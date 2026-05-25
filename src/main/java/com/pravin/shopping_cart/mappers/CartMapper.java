package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.CartDto;
import com.pravin.shopping_cart.dto.CartItemDto;
import com.pravin.shopping_cart.entities.Cart;
import com.pravin.shopping_cart.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

   @Mapper(componentModel = "spring")
   public interface CartMapper {

//      @Mapping(target = "items", source = "cartItems")
      CartDto toDto(Cart cart);

      @Mapping(target = "totalPrice", expression = "java(cartItem.getToalPrice())")
      CartItemDto toDto(CartItem cartItem);

   }
