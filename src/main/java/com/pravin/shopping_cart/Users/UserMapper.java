package com.pravin.shopping_cart.Users;

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
