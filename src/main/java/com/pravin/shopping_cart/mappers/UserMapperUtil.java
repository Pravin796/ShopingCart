package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.RegisterUserRequest;
import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.User;

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

    public static UserDto toEntity(RegisterUserRequest userDto){

    }
}
