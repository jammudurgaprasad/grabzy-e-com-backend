package com.example.ecombackend.seller.service;


import com.example.ecombackend.seller.models.Seller;
import com.example.ecombackend.seller.repository.SellerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder PasswordEncoder = new BCryptPasswordEncoder();

    public SellerService(SellerRepository sellerRepository){
        this.sellerRepository = sellerRepository;
    }

//    public Seller saveSeller(Seller seller){
//        Optional<Seller> optionalSeller = sellerRepository.findByGmail(seller.getGmail());
//        if(optionalSeller.isPresent()){
//            throw  new RuntimeException("Seller with this email id already exists");
//        }
//        String encodedPassword = PasswordEncoder.encode(seller.getPassword());
//        seller.setPassword(encodedPassword);
//        return sellerRepository.save(seller);
//    }

    public Seller saveSeller(Seller seller) {
        Optional<Seller> optionalSeller = sellerRepository.findByGmail(seller.getGmail());

        if (optionalSeller.isPresent()) {
            // If email belongs to a different seller, throw error
            if (!optionalSeller.get().getSellerId().equals(seller.getSellerId())) {
                throw new RuntimeException("Seller with this email id already exists");
            }
        }

        // Only encode password if it's new (e.g., during registration)
        if (seller.getSellerId() == null || seller.getPassword() != null && !seller.getPassword().startsWith("$2a$")) {
            String encodedPassword = PasswordEncoder.encode(seller.getPassword());
            seller.setPassword(encodedPassword);
        }

        return sellerRepository.save(seller);
    }




    public Seller sellerLogin(Seller loginRequest){
        Optional<Seller> optionalSeller = sellerRepository.findByGmail(loginRequest.getGmail());
        if(optionalSeller.isPresent()){
            Seller seller = optionalSeller.get();
            boolean isPasswordMatch = PasswordEncoder.matches(loginRequest.getPassword(), seller.getPassword());
            if(isPasswordMatch){
                return seller;
            }
        }
        throw new RuntimeException("Invalid sellername or password");
    }

    public List<Seller> findAll(){
        return sellerRepository.findAll();
    }

    public Optional<Seller> findById(Long sellerId){
        return sellerRepository.findBySellerId(sellerId);
    }

    public void deleteById(Long sellerId){
        sellerRepository.deleteById(sellerId);
    }

}
