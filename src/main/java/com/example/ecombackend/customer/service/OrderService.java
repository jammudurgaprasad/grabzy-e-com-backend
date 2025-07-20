package com.example.ecombackend.customer.service;

import com.example.ecombackend.customer.models.Order;
import com.example.ecombackend.customer.models.Users;
import com.example.ecombackend.customer.repository.OrderRepository;
import com.example.ecombackend.customer.repository.UserRepository;
import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.models.Seller;
import com.example.ecombackend.seller.repository.ProductRepository;
import com.example.ecombackend.seller.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        SellerRepository sellerRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    /* ----------- place a new order ----------- */

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }


    public List<Order> saveAll(List<Order> orders) {
        return orderRepository.saveAll(orders);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    public List<Order> placeMultipleOrders(List<Order> orders) {
        for (Order order : orders) {
            Product product = productRepository.findById(order.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            order.setProductName(product.getProductName());
            order.setActualPrice(product.getActualPrice());
            order.setDiscountPercentage(product.getDiscountPercentage());
            order.setDiscountPrice(product.getDiscountPrice());
            order.setDescription(product.getDescription());
            order.setSellerId(product.getSeller().getSellerId()); // Auto-fill sellerId
            order.setImage1(product.getImage1()); // ✅ NEW
        }
        return orderRepository.saveAll(orders);
    }

    /* ----------- read methods ----------- */
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /* ----------- update only order status ----------- */
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
