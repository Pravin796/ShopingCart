package com.pravin.shopping_cart.Users;

import java.time.LocalDateTime;

public class UserMapperUtil {
    public static UserDto toDto(User user){
        if(user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User toEntity(RegisterUserRequest request) {

        if (request == null) {
            return null;
        }

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }


}
