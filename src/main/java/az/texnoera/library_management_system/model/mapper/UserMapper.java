package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.response.UserResponse;

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
}
