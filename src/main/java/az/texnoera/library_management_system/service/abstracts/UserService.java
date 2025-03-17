package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.model.response.UserResponse;

public interface UserService {
    String createUser(UserRequest userRequest);
    String verifyOtp(String otp);
}
