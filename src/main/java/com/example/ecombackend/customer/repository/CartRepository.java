package com.example.ecombackend.customer.repository;

import com.example.ecombackend.customer.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);

    Cart findByUserIdAndProductProductIdAndSize(Long userId, Long productId, String size);
}