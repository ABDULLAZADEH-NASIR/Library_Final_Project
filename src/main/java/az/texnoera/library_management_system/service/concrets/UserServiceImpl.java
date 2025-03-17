package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.mapper.UserMapper;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        }
        else {
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
}
