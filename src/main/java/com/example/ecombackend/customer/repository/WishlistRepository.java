package com.example.ecombackend.customer.repository;

import com.example.ecombackend.customer.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndProductProductId(Long userId, Long productId);
    void deleteByUserIdAndProductProductId(Long userId, Long productId);

}
