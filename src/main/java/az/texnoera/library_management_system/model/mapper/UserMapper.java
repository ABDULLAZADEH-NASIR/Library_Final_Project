package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
import az.texnoera.library_management_system.model.response.BookCheckoutResponseForUser;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;

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
                .totalFineAmount(String.valueOf(user.getTotalFineAmount()))
                .build();
    }

    static UserResponseWithBookCheckout userToUserResponseWithCheckout(User user) {
        return UserResponseWithBookCheckout.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .FIN(user.getFIN())
                .email(user.getEmail())
                .BookCheckouts(user.getBookCheckouts().stream().map(b ->
                        BookCheckoutResponseForUser.builder()
                                .checkoutBookId(b.getId())
                                .bookName(b.getBook().getName())
                                .build()).collect(Collectors.toSet()))
                .totalFineAmount(String.valueOf(user.getTotalFineAmount()))
                .build();
    }

    static void userUpdateRequestToUser(User user, UserRequestForUpdate userRequest) {
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
    }
}
