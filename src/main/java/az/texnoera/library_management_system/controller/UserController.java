package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/create")
    public String create(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }


    @PostMapping("/verify-OTP")
    public String verifyOTP( @RequestBody String otp) {
        return userService.verifyOtp(otp);
    }


}
