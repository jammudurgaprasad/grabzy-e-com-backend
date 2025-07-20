package com.example.ecombackend.deliveryagent.repository;

import com.example.ecombackend.deliveryagent.models.AgentOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentOrdersRepository extends JpaRepository<AgentOrders, Long> {
    List<AgentOrders> findByDeliveryAgentAgentId(Long agentId);

    List<AgentOrders> findByDeliveryAgentAgentIdAndOrderStatusNotIn(Long agentId, List<String> statuses);
}