package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.LoginRequest;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
// Burda ancaq registr və login ilə bağlı olan ve public olan APİ-lardır
public class AuthController {
    private final UserServiceImpl userService;

    // Yeni useri registr edir
    @PostMapping("/register")
    public String register(@RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    // Userin daxil etdiyi OTP kod eger düzdürsə yalniz o zaman Useri DB-ə save edir
    @PostMapping("/verify-OTP")
    public String verifyOTP(@RequestBody int otp) {
        return userService.verifyOtp(otp);
    }

    // Save olunan Userin Login olur ve bu zaman roluna görə JWT alır
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
