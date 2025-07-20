package com.example.ecombackend.deliveryagent.repository;

import com.example.ecombackend.deliveryagent.models.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    Optional<DeliveryAgent> findByGmail(String gmail);

}
