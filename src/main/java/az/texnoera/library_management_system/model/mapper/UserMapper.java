package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.BorrowBookResponseForUser;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBorrow;

import java.util.stream.Collectors;

public interface UserMapper {
    static User userRequestToUser(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .FIN(userRequest.getFIN())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    static UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .FIN(user.getFIN())
                .email(user.getEmail())
                .totalDebitAzn(String.valueOf(user.getTotalDebtAzn()))
                .build();
    }

    static UserResponseWithBorrow userToUserResponseWithBorrow(User user) {
        return UserResponseWithBorrow.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .FIN(user.getFIN())
                .email(user.getEmail())
                .borrowedBooks(user.getBorrowedBooks().stream().map(b->
                        BorrowBookResponseForUser.builder()
                                .borrowedBookId(b.getId())
                                .BookName(b.getBook().getName())
                                .build()).collect(Collectors.toSet()))
                .totalDebtAzn(String.valueOf(user.getTotalDebtAzn()))
                .build();
    }

    static void userUpdateRequestToUser(User user, UserRequestForUpdate userRequest) {
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
    }
}
