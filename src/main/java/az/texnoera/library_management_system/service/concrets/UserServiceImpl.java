package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Role;
import az.texnoera.library_management_system.model.request.LoginRequest;
import az.texnoera.library_management_system.repo.RoleRepo;
import az.texnoera.library_management_system.security.utilities.JwtUtils;
import az.texnoera.library_management_system.service.notification.NotificationService;
import az.texnoera.library_management_system.service.otp.OtpService;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception.ApiException;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.UserMapper;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final OtpService otpService;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private User tempUser;

    // Yeni useri registr edir
    @Transactional
    @Override
    public String register(UserRequest userRequest) {
        log.info("Register method called with email: {}", userRequest.getMail());
        User user = UserMapper.userRequestToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> {
                    log.warn("ROLE_USER not found, creating new.");
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepo.save(newRole);
                });

        user.setRoles(Set.of(role));
        int otp = otpService.generateOtp();
        otpService.saveOtp(userRequest.getMail(), otp);
        boolean emailSend = otpService.sendOtpEmail(user.getEmail(), otp);
        this.tempUser = user;

        if (emailSend) {
            log.info("OTP sent successfully to email: {}", user.getEmail());
            return "OTP has been sent to your email. Please verify...";
        } else {
            log.error("OTP generated but email couldn't be sent to: {}", user.getEmail());
            tempUser = null;
            return "OTP generated, but email could not be sent. Please try again or contact support.";
        }
    }

    // Userin daxil etdiyi OTP kod eger düzdürsə yalniz o zaman Useri DB-ə save edir
    @Override
    public String verifyOtp(int otp) {
        log.info("Verifying OTP: {}", otp);
        if (tempUser == null) {
            log.warn("No registration in progress while verifying OTP");
            return "No registration process is currently active.";
        }
        if (otpService.validateOtp(tempUser.getEmail(), otp)) {
            userRepo.save(tempUser);
            log.info("User registered successfully with email: {}", tempUser.getEmail());
            tempUser = null;
            return "User successfully registered!";
        } else {
            log.warn("Invalid OTP for email: {}", tempUser.getEmail());
            return "Invalid OTP. Please try again.";
        }
    }

    // Save olunan Userin Login olur ve bu zaman roluna görə JWT alır
    public String login(LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getMail());
        User user = userRepo.findByEmail(loginRequest.getMail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", loginRequest.getMail());
                    return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.error("Incorrect password for email: {}", loginRequest.getMail());
            throw new ApiException(HttpStatus.UNAUTHORIZED, StatusCode.EMAIL_OR_PASSWORD_INCORRECT);
        }

        String token = jwtUtils.generateJwtToken(user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList());
        log.info("Login successful for email: {}", loginRequest.getMail());
        return token;
    }

    // User öz profilinə baxır
    @Override
    public UserResponseWithBookCheckout getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Fetching profile for current user: {}", email);
        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("Current user not found in DB: {}", email);
                    return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
                });
        return UserMapper.userToUserResponseWithCheckout(user);
    }

    // Useri id ilə gətirir
    @Override
    public UserResponse getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepo.findById(id).orElseThrow(() -> {
            log.error("User not found with id: {}", id);
            return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
        });
        return UserMapper.userToUserResponse(user);
    }

    // Useri oz BookCheckoutları ilə id-nə görə göstərir
    @Override
    public UserResponseWithBookCheckout getUserWithCheckoutsById(Long id) {
        log.info("Fetching user with checkouts by id: {}", id);
        User user = userRepo.findUserWithBorrow(id).orElseThrow(() -> {
            log.error("User with checkouts not found by id: {}", id);
            return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
        });
        return UserMapper.userToUserResponseWithCheckout(user);
    }

    // Bütün userləri göstərir
    @Override
    public Result<UserResponse> getAllUsers(int page, int size) {
        log.info("Fetching all users - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAllUsers(pageable);
        List<UserResponse> userList = users.getContent().stream()
                .map(UserMapper::userToUserResponse).toList();
        return new Result<>(userList, page, size, users.getTotalPages());
    }

    // Useri id ilə silir
    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user by id: {}", id);
        User user = userRepo.findById(id).orElseThrow(() -> {
            log.error("User not found while deleting with id: {}", id);
            return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
        });
        userRepo.delete(user);
        log.info("User deleted successfully with id: {}", id);
    }

    // Useri id ilə update edir
    @Transactional
    @Override
    public UserResponse updateUserById(Long id, UserRequestForUpdate userRequest) {
        log.info("Updating user with id: {}", id);
        User user = userRepo.findById(id).orElseThrow(() -> {
            log.error("User not found while updating with id: {}", id);
            return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
        });
        UserMapper.userUpdateRequestToUser(user, userRequest);
        UserResponse updated = UserMapper.userToUserResponse(userRepo.save(user));
        log.info("User updated successfully with id: {}", id);
        return updated;
    }

    // Useri FİN ilə göstərir
    @Override
    public UserResponseWithBookCheckout getUserByFin(String fin) {
        log.info("Fetching user by FIN: {}", fin);
        User user = userRepo.findUserByFIN(fin).orElseThrow(() -> {
            log.error("User not found with FIN: {}", fin);
            return new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND);
        });
        return UserMapper.userToUserResponseWithCheckout(user);
    }

    // Qaytarılma vaxtı keçən hər dəqiqəyə görə 1 AZN cərimə hesablanır.
    // Yəni hər qaytarılması gecikən kitabın borcu BookCheckout yazılır və
    // həmçinin Userin ümumi borcu hesablanır Userin Emailinə borc bildirişi göndərilir

    @Transactional
    @Scheduled(cron = "0 */5 * * * ?")
    public void sendScheduledDebtNotifications() {
        log.info("Scheduled task started: sending debt notifications.");
        List<User> users = userRepo.findAllUsersWithBorrowedBooks();

        // Umumi olarag hem kitabin hemde userin borclarini yenileyir (Borc bildirisi yollamaq ucun)
        for (User user : users) {
            for (BookCheckout bookCheckout : user.getBookCheckouts()) {
                bookCheckout.calculateFine(); // Kitabin borcunu yenileyir
            }
            user.updateTotalDebt(); // Umumi borcu yenileyir
            userRepo.save(user); // Yenilenmiw borcu DB-e yazir

            if (user.getTotalFineAmount().compareTo(BigDecimal.ZERO) > 0) {
                log.info("User with email {} has debt: {}", user.getEmail(), user.getTotalFineAmount());
                notificationService.sendMailDebtMessage(user);
            }
        }
        log.info("Scheduled debt notification task completed.");
    }
}