package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.LoginRequest;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserServiceImpl userService;


    @PostMapping("/register")
    public String register(@RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    @PostMapping("/verify-OTP")
    public String verifyOTP(@RequestBody int otp) {
        return userService.verifyOtp(otp);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

}
