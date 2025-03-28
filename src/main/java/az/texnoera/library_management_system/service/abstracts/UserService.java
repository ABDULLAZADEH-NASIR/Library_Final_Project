package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;

public interface UserService {
    String createUser(UserRequest userRequest);

    String verifyOtp(int otp);

    UserResponse getUserById(Long id);

    UserResponseWithBorrow getUserWithBorrowsById(Long id);

    Result<UserResponse> getAllUsers(int page, int size);

    void deleteUserById(Long id);

    UserResponse updateUserById(Long id, UserRequestForUpdate userRequest);

    UserResponseWithBorrow getUserByFin(String fin);
}
