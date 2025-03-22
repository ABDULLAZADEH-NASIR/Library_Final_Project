package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.config.NotificationService;
import az.texnoera.library_management_system.config.OtpService;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final OtpService otpService;

    private User tempUser;

    @Override
    public String createUser(UserRequest userRequest) {
        User user = UserMapper.userRequestForUser(userRequest);
        String otp = otpService.generateOtp();
        otpService.saveOtp(userRequest.getEmail(), otp);
        this.tempUser = user;
        otpService.sendOtpEmail(userRequest.getEmail(), otp);
        return ("OTP has been sent to your email. Please verify...");
    }

    @Override
    public String verifyOtp(String otp) {
        if (tempUser == null) {
            return "No registration process is currently active.";
        }

        // OTP doğrulaması
        if (otpService.validateOtp(tempUser.getEmail(), otp)) {
            userRepo.save(tempUser);
            tempUser = null;
            return "User successfully registered!";
        } else {
            return "Invalid OTP. Please try again.";
        }

    }
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        return UserMapper.userForUserResponse(user);
    }

    @Override
    public UserResponseWithBorrow getUserBorrowedById(Long id) {
        User user = userRepo.findUserWithBorrow(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
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
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        userRepo.delete(user);
    }

    @Transactional
    @Override
    public UserResponse updateUserById(Long id, UserRequestForUpdate userRequest) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        UserMapper.userUpdateRequestForUser(user, userRequest);
        return UserMapper.userForUserResponse(userRepo.save(user));

    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void sendScheduledDebtNotifications() {
        sendDailyDebtNotifications();
    }

    public void sendDailyDebtNotifications() {
        for (User user : userRepo.findAll()) {
            if (user.calculateTotalDebt().compareTo(BigDecimal.ZERO) > 0) {
                notificationService.sendMailDebtMessage(user);  // NotificationService çağırılır
            }
        }
    }
}
