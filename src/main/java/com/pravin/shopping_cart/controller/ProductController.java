package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.ProductDto;
import com.pravin.shopping_cart.entities.Product;
import com.pravin.shopping_cart.mappers.ProductMapperUtil;
import com.pravin.shopping_cart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
//    private final ProductMapperUtil productMapperUtil;

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ){
//        if(categoryId )
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        }else{
            products = productRepository.findAll();
        }
       return  products.stream().map(ProductMapperUtil::toDto).toList();
    }
}
