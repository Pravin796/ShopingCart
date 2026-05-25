package com.pravin.shopping_cart.mappers;

import com.pravin.shopping_cart.dto.ProductDto;
import com.pravin.shopping_cart.entities.Product;
import com.pravin.shopping_cart.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto product);

    @Mapping(target = "id", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);

}
