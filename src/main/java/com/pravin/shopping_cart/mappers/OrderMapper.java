package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.OrderDto;
import com.pravin.shopping_cart.entities.Order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
