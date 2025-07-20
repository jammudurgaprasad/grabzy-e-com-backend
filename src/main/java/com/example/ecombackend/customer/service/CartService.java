package com.example.ecombackend.customer.service;

import com.example.ecombackend.customer.models.Cart;
import com.example.ecombackend.customer.repository.CartRepository;
import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.repository.ProductRepository;
import com.example.ecombackend.customer.models.Users;
import com.example.ecombackend.customer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    public Cart addToCart(Cart cart) {
        if (cart.getProduct() == null || cart.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required.");
        }

        if (cart.getUser() == null || cart.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID is required.");
        }

        Long productId = cart.getProduct().getProductId();
        Long userId = cart.getUser().getId();
        String size = cart.getSize();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if product with same size already exists in user's cart
        Cart existingCart = cartRepository.findByUserIdAndProductProductIdAndSize(userId, productId, size);

        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            return cartRepository.save(existingCart);
        }

        cart.setProduct(product);
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public void deleteCartByUserId(Long userId) {
        List<Cart> userCart = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(userCart);
    }




    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart updateCart(Long cartId, Cart updatedCart) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        Product product = productRepository.findById(updatedCart.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Users user = userRepository.findById(updatedCart.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(updatedCart.getQuantity());
        cart.setSize(updatedCart.getSize()); // 👈 Set size

        return cartRepository.save(cart);
    }

    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
