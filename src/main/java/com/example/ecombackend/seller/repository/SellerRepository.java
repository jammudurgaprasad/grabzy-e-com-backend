package com.example.ecombackend.seller.repository;

import com.example.ecombackend.seller.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findBySellerId(Long sellerId);

    Optional<Seller> findByGmail(String gmail);
}
