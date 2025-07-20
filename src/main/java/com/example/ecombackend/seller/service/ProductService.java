package com.example.ecombackend.seller.service;

import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long productId){
        return productRepository.findByProductId(productId);
    }

    public void deleteById(Long productId){
        productRepository.deleteById(productId);
    }

    public List<Product> findBySellerId(Long sellerId){
        return productRepository.findBySellerSellerId(sellerId);
    }
}
