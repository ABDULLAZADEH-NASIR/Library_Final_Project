package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.mapper.UserMapper;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final JavaMailSender mailSender;
    private static final int OTP_VALID_MINUTES = 5;

    private final Map<String, String> otpStore = new HashMap<>();
    private User tempUser;

    public String createUser(UserRequest userRequest) {
        User user = UserMapper.userRequestForUser(userRequest);
        String otp = generateOtp();

        // OTP-ni müvəqqəti saxlayıram
        otpStore.put(userRequest.getEmail(), otp);
        this.tempUser = user; // Müvəqqəti istifadəçini yadda saxlayıram

        // İstifadəçiyə emailine OTP kodu gonderirem
        sendOtpEmail(userRequest.getEmail(), otp);
        return ("OTP has been sent to your email. Please verify...");
    }

    public String verifyOtp(String otp) {
        if (tempUser == null || !otpStore.containsKey(tempUser.getEmail())) {
            return "No registration process is currently active.";
        }

        String storedOtp = otpStore.get(tempUser.getEmail());

        if (storedOtp.equals(otp)) {
            userRepo.save(tempUser);
            otpStore.remove(tempUser.getEmail()); // OTP-ni yaddaşdan silirəm..
            // hansiki muveqqeti yaddasa vermisdim
            tempUser = null;
            return "User successfully registered!";
        } else {
            return "Invalid OTP. Please try again.";
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // 4 rəqəmli OTP kodu generate edirəm
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification");
        message.setText("Your OTP code is: " + otp + ". It will expire in "
                + OTP_VALID_MINUTES + " minutes.");
        mailSender.send(message);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        return UserMapper.userForUserResponse(user);
    }

    @Override
    public UserResponseWithBorrow getUserBorrowedById(Long id) {
        User user = userRepo.findUserWithBorrow(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        return UserMapper.userForUserResponseWithBorrow(user);
    }

    @Override
    public List<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAllUsers(pageable);
        return users.stream().map(UserMapper::userForUserResponse).toList();
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        userRepo.delete(user);
    }

    @Transactional
    @Override
    public UserResponse updateUserById(Long id, UserRequestForUpdate userRequest) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        UserMapper.userUpdateRequestForUser(user, userRequest);
        return UserMapper.userForUserResponse(userRepo.save(user));

    }

    @Scheduled(cron = "0 0 9 * * ?")  // Hər gün saat 9:00-da işləyəcək
    public void sendScheduledDebtNotifications() {
        sendDailyDebtNotifications();  // Borcu olan istifadəçilərə e-mail göndərmək
    }

    public void sendMailDebtMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Library Debt Notification");

        StringBuilder fineDetails = new StringBuilder("Dear " + user.getName() + " " + user.getSurname() + ",\n\n" +
                "The following books have been overdue, and fines have been calculated:\n");

        BigDecimal totalDebt = user.calculateTotalDebt();  // Ümumi borcu hesablayir

        for (BorrowBook borrowBook : user.getBorrowedBooks()) {
            borrowBook.calculateFine();  // Hər kitabın cərməsini hesabla

            if (borrowBook.getFineAmountAZN().compareTo(BigDecimal.ZERO) > 0) {  // Gecikmiş kitablar üçün kitab adi
                // ve hemin kitab uzre borcu gosterir
                fineDetails.append("- ").append(borrowBook.getBook().getName())
                        .append(": ").append(borrowBook.getFineAmountAZN()).append(" AZN\n");
            }
        }

        fineDetails.append("\nYour total debt amount is: ").append(totalDebt).append(" AZN.\n\n")
                .append("To avoid higher fines, we kindly request that " +
                        "you return the books and settle your debt as soon as possible.\n\n")
                .append("Thank you for your attention.\n\n")
                .append("Sincerely,\n")
                .append("Library Management System");

        message.setText(fineDetails.toString());
        mailSender.send(message);
    }

    public void sendDailyDebtNotifications() {
        for (User user : userRepo.findAll()) {
            if (user.calculateTotalDebt().compareTo(BigDecimal.ZERO) > 0) {
                sendMailDebtMessage(user);  // Borcu olan istifadəçilərə mail massage göndərirem
            }
        }
    }
}
