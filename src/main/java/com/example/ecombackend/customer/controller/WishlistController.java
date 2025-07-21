package com.example.ecombackend.customer.controller;

import com.example.ecombackend.customer.models.Wishlist;
import com.example.ecombackend.customer.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = {"https://dpgrabzy.netlify.app","https://dpgrabzyhub.netlify.app","https://grabzy-e-com-frontend.vercel.app/"}, allowCredentials = "true")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public Wishlist addToWishlist(@RequestBody Wishlist wishlist) {
        return wishlistService.addToWishlist(wishlist);
    }

    @GetMapping("/user/{userId}")
    public List<Wishlist> getWishlistByUser(@PathVariable Long userId) {
        return wishlistService.getWishlistByUser(userId);
    }

    @DeleteMapping("/{wishlistId}")
    public void removeFromWishlist(@PathVariable Long wishlistId) {
        wishlistService.removeFromWishlist(wishlistId);
    }

    @GetMapping("/check")
    public boolean checkWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        return wishlistService.isInWishlist(userId, productId);
    }

    @DeleteMapping("/remove")
    public void removeFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
    }

}
