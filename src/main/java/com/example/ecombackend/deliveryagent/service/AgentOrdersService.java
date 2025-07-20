package com.example.ecombackend.deliveryagent.service;

import com.example.ecombackend.customer.models.Order;
import com.example.ecombackend.customer.repository.OrderRepository;
import com.example.ecombackend.deliveryagent.models.AgentOrders;
import com.example.ecombackend.deliveryagent.models.DeliveryAgent;
import com.example.ecombackend.deliveryagent.repository.AgentOrdersRepository;
import com.example.ecombackend.deliveryagent.repository.DeliveryAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentOrdersService {

    @Autowired
    private AgentOrdersRepository agentOrdersRepository;

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public AgentOrders assignOrderToAgent(Long agentId, Long orderId) {
        DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        AgentOrders agentOrder = new AgentOrders();
        agentOrder.setDeliveryAgent(agent);
        agentOrder.setOrder(order);

        return agentOrdersRepository.save(agentOrder);
    }

    public List<AgentOrders> getOrdersByAgent(Long agentId) {
        return agentOrdersRepository.findByDeliveryAgentAgentId(agentId);
    }

    public AgentOrders updateOrderStatus(Long agentOrderId, String status) {
        AgentOrders agentOrder = agentOrdersRepository.findById(agentOrderId)
                .orElseThrow(() -> new RuntimeException("AgentOrder not found"));

        return agentOrdersRepository.save(agentOrder);
    }

    public List<AgentOrders> getAllAgentOrders() {
        return agentOrdersRepository.findAll();
    }

    public List<AgentOrders> getActiveOrdersByAgent(Long agentId) {
        return agentOrdersRepository.findByDeliveryAgentAgentIdAndOrderStatusNotIn(
                agentId, List.of("PENDING", "DELIVERED")
        );
    }


}
