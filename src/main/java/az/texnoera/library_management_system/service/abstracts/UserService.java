package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    String createUser(UserRequest userRequest);
    String verifyOtp(String otp);

    UserResponse getUserById(Long id);

    UserResponseWithBorrow getUserBorrowedById(Long id);

    List<UserResponse> getAllUsers(int page, int size);

    void deleteUserById(Long id);

    UserResponse updateUserById(Long id, UserRequestForUpdate userRequest);

}
