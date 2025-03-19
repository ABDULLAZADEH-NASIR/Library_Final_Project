package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.BorrowBookResponseForUser;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;

import java.util.stream.Collectors;

public interface UserMapper {
    static User userRequestForUser(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    static UserResponse userForUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    static UserResponseWithBorrow userForUserResponseWithBorrow(User user) {
        return UserResponseWithBorrow.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .borrowedBooks(user.getBorrowedBooks().stream().map(b->
                        BorrowBookResponseForUser.builder()
                                .borrowedBookId(b.getId())
                                .BookName(b.getBook().getName())
                                .build()).collect(Collectors.toSet()))
                .build();
    }

    static void userUpdateRequestForUser(User user, UserRequestForUpdate userRequest) {
        User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .build();
    }
}
