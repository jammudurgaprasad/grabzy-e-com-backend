package com.example.ecombackend.customer.service;

import com.example.ecombackend.customer.models.Users;
import com.example.ecombackend.customer.models.Wishlist;
import com.example.ecombackend.customer.repository.UserRepository;
import com.example.ecombackend.customer.repository.WishlistRepository;
import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Wishlist addToWishlist(Wishlist wishlist) {
        Long userId = wishlist.getUser().getId();
        Long productId = wishlist.getProduct().getProductId();

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getWishlistByUser(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public void removeFromWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.findByUserIdAndProductProductId(userId, productId).isPresent();
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.deleteByUserIdAndProductProductId(userId, productId);
    }

}
