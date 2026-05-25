package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.CartDto;
import com.pravin.shopping_cart.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

   CartDto toDto(Cart cart);
}
