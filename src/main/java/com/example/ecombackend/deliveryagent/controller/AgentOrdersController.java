package com.example.ecombackend.deliveryagent.controller;

import com.example.ecombackend.deliveryagent.models.AgentOrders;
import com.example.ecombackend.deliveryagent.service.AgentOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent-orders")
@CrossOrigin(origins = {"https://dpgrabzy.netlify.app","https://dpgrabzyhub.netlify.app","https://grabzy-e-com-frontend.vercel.app/"}, allowCredentials = "true")
public class AgentOrdersController {

    @Autowired
    private AgentOrdersService agentOrdersService;

    @GetMapping
    public List<AgentOrders> getAllAgentOrders() {
        return agentOrdersService.getAllAgentOrders();
    }


    @PostMapping("/assign")
    public AgentOrders assignOrderToAgent(@RequestParam Long agentId, @RequestParam Long orderId) {
        return agentOrdersService.assignOrderToAgent(agentId, orderId);
    }

    @GetMapping("/agent/{agentId}")
    public List<AgentOrders> getOrdersByAgent(@PathVariable Long agentId) {
        return agentOrdersService.getOrdersByAgent(agentId);
    }

    @PutMapping("/{id}/status")
    public AgentOrders updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return agentOrdersService.updateOrderStatus(id, status);
    }

    @GetMapping("/agent/{agentId}/active")
    public List<AgentOrders> getActiveOrdersByAgent(@PathVariable Long agentId) {
        return agentOrdersService.getActiveOrdersByAgent(agentId);
    }

}
