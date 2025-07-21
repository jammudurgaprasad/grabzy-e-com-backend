package com.example.ecombackend.seller.controller;

import com.example.ecombackend.seller.models.SellerOrders;
import com.example.ecombackend.seller.service.SellerOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller-orders")
@CrossOrigin(origins = {"https://dpgrabzy.netlify.app","https://dpgrabzyhub.netlify.app","https://grabzy-e-com-frontend.vercel.app/"}, allowCredentials = "true")
public class SellerOrdersController {

    @Autowired
    private SellerOrdersService sellerOrdersService;

    @PostMapping("/create")
    public SellerOrders createSellerOrder(@RequestParam Long orderId, @RequestParam Long agentId) {
        return sellerOrdersService.createSellerOrder(orderId, agentId);
    }

    @PutMapping("/{id}/status")
    public SellerOrders updateStatus(@PathVariable Long id, @RequestParam String status) {
        return sellerOrdersService.updateOrderStatus(id, status);
    }

    @GetMapping("/agent/{agentId}")
    public List<SellerOrders> getOrdersByAgent(@PathVariable Long agentId) {
        return sellerOrdersService.getOrdersByAgentId(agentId);
    }

    @GetMapping("/status")
    public List<SellerOrders> getOrdersByStatus(@RequestParam String status) {
        return sellerOrdersService.getOrdersByStatus(status);
    }
}
