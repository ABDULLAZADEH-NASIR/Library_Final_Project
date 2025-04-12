package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Role;
import az.texnoera.library_management_system.model.request.LoginRequest;
import az.texnoera.library_management_system.repo.RoleRepo;
import az.texnoera.library_management_system.security.utilities.JwtUtils;
import az.texnoera.library_management_system.utils.NotificationService;
import az.texnoera.library_management_system.utils.OtpService;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private User tempUser;

    @Transactional
    @Override
    public String register(UserRequest userRequest) {
        User user = UserMapper.userRequestToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepo.save(newRole);
                });

        user.setRoles(Set.of(role));
        int otp = otpService.generateOtp();
        otpService.saveOtp(userRequest.getEmail(), otp);
        boolean emailSend = otpService.sendOtpEmail(user.getEmail(), otp);
        this.tempUser = user;

        if (emailSend) {
            return "OTP has been sent to your email. Please verify...";
        } else {
            tempUser = null;
            return "OTP generated, but email could not be sent. Please try again or contact support.";
        }
    }


    @Override
    public String verifyOtp(int otp) {
        if (tempUser == null) {
            return "No registration process is currently active.";
        }
        if (otpService.validateOtp(tempUser.getEmail(), otp)) {
            userRepo.save(tempUser);
            tempUser = null;
            return "User successfully registered!";
        } else {
            return "Invalid OTP. Please try again.";
        }
    }


    public String login(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getMail())
                .orElseThrow(() -> new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BasedExceptions(HttpStatus.UNAUTHORIZED,StatusCode.EMAIL_OR_PASSWORD_INCORRECT);
        }
        return jwtUtils.generateJwtToken(user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList());
    }

    @Override
    public UserResponseWithBookCheckout getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        return UserMapper.userToUserResponseWithCheckout(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        return UserMapper.userToUserResponse(user);
    }

    @Override
    public UserResponseWithBookCheckout getUserWithCheckoutsById(Long id) {
        User user = userRepo.findUserWithBorrow(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        return UserMapper.userToUserResponseWithCheckout(user);
    }

    @Override
    public Result<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAllUsers(pageable);
        List<UserResponse> userList = users.getContent().stream()
                .map(UserMapper::userToUserResponse).toList();
        return new Result<>(userList, page, size, users.getTotalPages());
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
        UserMapper.userUpdateRequestToUser(user, userRequest);
        return UserMapper.userToUserResponse(userRepo.save(user));

    }

    @Override
    public UserResponseWithBookCheckout getUserByFin(String fin) {
        User user = userRepo.findUserByFIN(fin).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));
        return UserMapper.userToUserResponseWithCheckout(user);
    }


    @Transactional
    @Scheduled(cron = "0 */5 * * * ?")
    public void sendScheduledDebtNotifications() {
        List<User> users = userRepo.findAllUsersWithBorrowedBooks();

        // Umumi olarag hem kitabin hemde userin borclarini yenileyir (Borc bildirisi yollamaq ucun)
        for (User user : users) { // Set-i birbaşa iterasiya ede bilərik
            for (BookCheckout bookCheckout : user.getBookCheckouts()) {
                bookCheckout.calculateFine(); // Kitabin borcunu yenileyir
            }

            user.updateTotalDebt(); // Umumi borcu yenileyir
            userRepo.save(user); // Yenilenmiw borcu DB-e yazir

            // Eger borc varsa email gonderir
            if (user.getTotalFineAmount().compareTo(BigDecimal.ZERO) > 0) {
                notificationService.sendMailDebtMessage(user);
            }
        }
    }
}
