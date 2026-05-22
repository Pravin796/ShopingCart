package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.ProductDto;
import com.pravin.shopping_cart.entities.Product;
import com.pravin.shopping_cart.mappers.ProductMapper;
import com.pravin.shopping_cart.mappers.ProductMapperUtil;
import com.pravin.shopping_cart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestHeader(required = false, value = "x-auth-token") String authToken,
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ){
        System.out.println(authToken);
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategory_Id(categoryId);
        }else{
            products = productRepository.findAllWithCategeory();
        }
       return  products.stream().map(productMapper::toDto).toList();
    }
}
