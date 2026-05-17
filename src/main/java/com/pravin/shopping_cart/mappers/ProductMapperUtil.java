package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.ProductDto;
import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.Product;

import java.time.LocalDateTime;

public class ProductMapperUtil {
    public static ProductDto toDto(Product product){
        if(product == null) return null;

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )
                .build();

    }
}
