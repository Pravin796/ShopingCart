package com.pravin.shopping_cart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;


    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE,orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice(){
        BigDecimal total = BigDecimal.ZERO;
        for(CartItem item : items){
             total = total.add(item.getToalPrice());
        }
        return total;

//        same but in advance
//        return items.stream()
//                .map(CartItem::getToalPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(Long product){
        return items.stream()
                .filter(items -> items.getProduct().getId().equals(product))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = getItem(product.getId());

        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId){
        var cartItem = getItem(productId);
        if(cartItem != null){
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clear(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

}