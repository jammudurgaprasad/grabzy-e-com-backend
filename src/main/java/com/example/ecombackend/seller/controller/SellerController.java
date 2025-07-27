package com.example.ecombackend.seller.controller;


import com.example.ecombackend.seller.JwtSellerService;
import com.example.ecombackend.seller.models.Seller;
import com.example.ecombackend.seller.service.SellerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/seller")
@CrossOrigin(origins = {"http://localhost:3000","https://grabzy-e-com-frontend.netlify.app","https://grabzy-hub-e-com-frontend.netlify.app"}, allowCredentials = "true")
public class SellerController {

    private final SellerService sellerService;
    private final JwtSellerService jwtSellerService;

    public SellerController(SellerService sellerService, JwtSellerService jwtSellerService){
        this.sellerService = sellerService;
        this.jwtSellerService = jwtSellerService;
    }

    @PostMapping
    public ResponseEntity<?> createSeller(@RequestBody Seller seller){
        try{
            Seller savedSeller = sellerService.saveSeller(seller);
            return ResponseEntity.ok(savedSeller);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/seller-login")
    public ResponseEntity<?> sellerLogin(@RequestBody Seller credentials){
        try{
            Seller seller = sellerService.sellerLogin(credentials);
            String sellerToken = jwtSellerService.generateToken(seller.getGmail(), seller.getSellerId());

            ResponseCookie cookie = ResponseCookie.from("sellerJwt", sellerToken)
                    .httpOnly(true)
                    .secure(true) // Set to true in production
                    .path("/")
                    .maxAge(7*24*60*60)
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Seller Logged in successfully...");
        }catch (Exception e){
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/seller-logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("sellerJwt", "")
                .httpOnly(true)
                .secure(true) // true in production
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }



    @GetMapping("/check-seller-auth")
    public ResponseEntity<?> checkAuth(@CookieValue(name = "sellerJwt", required = false) String token) {
        if (token == null || !jwtSellerService.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String useremail = jwtSellerService.extractUserEmail(token);
        Long userId = jwtSellerService.extractUserId(token);

        return ResponseEntity.ok().body(Map.of(
                "useremail", useremail,
                "userId", userId
        ));
    }


    @GetMapping
    public List<Seller> getAllSellers(){
        return sellerService.findAll();
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long sellerId){
        return sellerService.findById(sellerId)
                .map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{sellerId}")
    public ResponseEntity<Seller> updateSellerById(@PathVariable Long sellerId, @RequestBody Seller updatedSeller) {
        return sellerService.findById(sellerId)
                .map(existingSeller -> {
                    existingSeller.setName(updatedSeller.getName());
                    existingSeller.setOrganizationName(updatedSeller.getOrganizationName());
                    existingSeller.setPhoneNo(updatedSeller.getPhoneNo());
                    existingSeller.setAddress(updatedSeller.getAddress());
                    existingSeller.setCity(updatedSeller.getCity());
                    existingSeller.setDistrict(updatedSeller.getDistrict());
                    existingSeller.setState(updatedSeller.getState());
                    existingSeller.setPinCode(updatedSeller.getPinCode());
                    // Do NOT update gmail or sensitive fields unless intended
                    return ResponseEntity.ok(sellerService.saveSeller(existingSeller));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Seller> deleteSellerById(@PathVariable Long sellerId){
        if(sellerService.findById(sellerId).isPresent()){
            sellerService.deleteById(sellerId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/{sellerId}/approve")
    public ResponseEntity<?> approveSeller(@PathVariable Long sellerId) {
        Optional<Seller> optionalSeller = sellerService.findById(sellerId);
        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            seller.setApproved(true);
            sellerService.saveSeller(seller);
            return ResponseEntity.ok("Seller approved");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{sellerId}/unapprove")
    public ResponseEntity<?> unapproveSeller(@PathVariable Long sellerId) {
        Optional<Seller> optionalSeller = sellerService.findById(sellerId);
        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            seller.setApproved(false);
            sellerService.saveSeller(seller);
            return ResponseEntity.ok("Seller unapproved");
        }
        return ResponseEntity.notFound().build();
    }


}
