package com.example.ecombackend.seller.repository;

import com.example.ecombackend.seller.models.SellerOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerOrdersRepository extends JpaRepository<SellerOrders, Long> {
    List<SellerOrders> findByDeliveryAgentAgentId(Long agentId);
    List<SellerOrders> findByStatus(String status);
}
