package com.example.ecombackend.seller.repository;

import com.example.ecombackend.seller.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(Long productId);
    List<Product> findBySellerSellerId(Long sellerId);
}
