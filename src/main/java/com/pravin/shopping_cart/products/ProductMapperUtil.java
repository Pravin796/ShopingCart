package com.pravin.shopping_cart.products;


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
