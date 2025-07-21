package com.example.ecombackend.customer.controller;

import com.example.ecombackend.customer.models.Cart;
import com.example.ecombackend.customer.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = {"https://dpgrabzy.netlify.app","https://dpgrabzyhub.netlify.app","https://grabzy-e-com-frontend.vercel.app/","https://grabzy-hub-e-com-frontend.vercel.app/"}, allowCredentials = "true")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/user/{userId}")
    public List<Cart> getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PutMapping("/{cartId}")
    public Cart updateCart(@PathVariable Long cartId, @RequestBody Cart cart) {
        return cartService.updateCart(cartId, cart);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteCartByUserId(@PathVariable Long userId) {
        cartService.deleteCartByUserId(userId);
    }

}
