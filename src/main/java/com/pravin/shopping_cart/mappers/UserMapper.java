package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.RegisterUserRequest;
import com.pravin.shopping_cart.dto.UpdateUserRequest;
import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = java.time.LocalDateTime.class)
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
