package com.example.ecombackend.customer.controller;

import com.example.ecombackend.customer.models.Order;
import com.example.ecombackend.customer.service.OrderService;
import com.example.ecombackend.deliveryagent.service.AgentOrdersService;
import com.example.ecombackend.seller.JwtSellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    private final OrderService orderService;
    private final JwtSellerService jwtSellerService;
    private final AgentOrdersService agentOrdersService;



    public OrderController(OrderService orderService, JwtSellerService jwtSellerService, AgentOrdersService agentOrdersService) {
        this.orderService = orderService;
        this.jwtSellerService = jwtSellerService;
        this.agentOrdersService = agentOrdersService;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeMultipleOrders(@RequestBody List<Order> orders) {
        try {
            List<Order> savedOrders = orderService.placeMultipleOrders(orders);
            return ResponseEntity.ok(savedOrders);
        } catch (Exception e) {
            e.printStackTrace(); // Already present
            return ResponseEntity.status(500).body("Order placement failed: " + e.getMessage()); // ✅ Show cause
        }
    }


    @GetMapping
    public List<Order> allOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Order not found");
        }
    }


    @GetMapping("/user/{userId}")
    public List<Order> ordersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long orderId,
                                              @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }

    @PutMapping("/{orderId}/assign-agent")
    public ResponseEntity<?> assignAgentToOrder(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> request,
            @CookieValue(name = "sellerJwt", required = false) String token
    ) {
        try {
            // 1. Auth check
            if (token == null || !jwtSellerService.validateToken(token)) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            Long sellerIdFromToken = jwtSellerService.extractUserId(token);
            Long agentId = Long.parseLong(request.get("agentId").toString());
            String status = request.get("status").toString();  // Expected: "SHIPPED"

            // 2. Fetch the order
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(404).body("Order not found");
            }

            // 3. Verify that the seller owns the order
            if (!order.getSellerId().equals(sellerIdFromToken)) {
                return ResponseEntity.status(403).body("Forbidden: You can only assign your own orders");
            }

            // 4. Update order status + agentId
//            order.setAgentId(agentId);
            order.setStatus(status);
            orderService.saveOrder(order);  // Persist update

            // 5. Add entry in AgentOrders table
            agentOrdersService.assignOrderToAgent(agentId, orderId);

            return ResponseEntity.ok("Order assigned to agent and marked as SHIPPED");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to assign agent: " + e.getMessage());
        }
    }


}

