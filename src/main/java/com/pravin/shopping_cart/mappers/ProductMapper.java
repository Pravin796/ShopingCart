package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.ProductDto;
import com.pravin.shopping_cart.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
}
