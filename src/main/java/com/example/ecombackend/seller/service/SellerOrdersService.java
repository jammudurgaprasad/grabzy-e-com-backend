package com.example.ecombackend.seller.service;

import com.example.ecombackend.customer.models.Order;
import com.example.ecombackend.customer.repository.OrderRepository;
import com.example.ecombackend.deliveryagent.models.DeliveryAgent;
import com.example.ecombackend.deliveryagent.repository.DeliveryAgentRepository;
import com.example.ecombackend.seller.models.SellerOrders;
import com.example.ecombackend.seller.repository.SellerOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerOrdersService {

    @Autowired
    private SellerOrdersRepository sellerOrdersRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    public SellerOrders createSellerOrder(Long orderId, Long agentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Delivery Agent not found"));

        SellerOrders sellerOrder = new SellerOrders();
        sellerOrder.setOrder(order);
        sellerOrder.setDeliveryAgent(agent);
        sellerOrder.setStatus("PENDING");

        return sellerOrdersRepository.save(sellerOrder);
    }

    public SellerOrders updateOrderStatus(Long id, String status) {
        SellerOrders sellerOrder = sellerOrdersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SellerOrder not found"));

        sellerOrder.setStatus(status);
        return sellerOrdersRepository.save(sellerOrder);
    }

    public List<SellerOrders> getOrdersByAgentId(Long agentId) {
        return sellerOrdersRepository.findByDeliveryAgentAgentId(agentId);
    }

    public List<SellerOrders> getOrdersByStatus(String status) {
        return sellerOrdersRepository.findByStatus(status);
    }
}
