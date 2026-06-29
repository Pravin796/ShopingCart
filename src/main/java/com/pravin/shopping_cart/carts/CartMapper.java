package com.pravin.shopping_cart.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

   @Mapper(componentModel = "spring")
   public interface CartMapper {

//      @Mapping(target = "items", source = "items")
        @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
      CartDto toDto(Cart cart);

      @Mapping(target = "totalPrice", expression = "java(cartItem.getToalPrice())")
      CartItemDto toDto(CartItem cartItem);

   }
