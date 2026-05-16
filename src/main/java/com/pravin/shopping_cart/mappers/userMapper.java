package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface userMapper {
    UserDto todo(User username);
}
