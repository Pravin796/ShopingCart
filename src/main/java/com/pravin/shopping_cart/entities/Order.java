package com.pravin.shopping_cart.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false) //for the hibernate dont worry about this time data database will handle it
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();

    public static Order fromCart(Cart cart, User customer){
        var order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        cart.getItems().forEach(items ->{
            var orderItem = new OrderItem(order, items.getProduct(), items.getQuantity());
            order.items.add(orderItem);
        });

        return order;
    }

    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }

}