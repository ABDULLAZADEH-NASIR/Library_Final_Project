package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}")
    public UserResponseWithBorrow getUserBorrowedById(@PathVariable Long id) {
        return  userService.getUserBorrowedById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(@RequestParam(defaultValue = "0")int page,
                                          @RequestParam(defaultValue = "10")int size) {
        return userService.getAllUsers(page,size);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUserById(@PathVariable Long id,
                                       @RequestBody UserRequestForUpdate userRequest) {
          return userService.updateUserById(id,userRequest);
    }

}
