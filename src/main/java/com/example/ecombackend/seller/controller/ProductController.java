package com.example.ecombackend.seller.controller;

import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = {"http://localhost:3000","https://grabzy-e-com-frontend.netlify.app","https://grabzy-hub-e-com-frontend.netlify.app"}, allowCredentials = "true")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.findAll();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId){
        return productService.findById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProductById(@PathVariable Long productId, @RequestBody Product product){
        return productService.findById(productId)
                .map(existingProduct -> {
                    existingProduct.setProductName(product.getProductName());
                    existingProduct.setActualPrice(product.getActualPrice());
                    existingProduct.setDiscountPercentage(product.getDiscountPercentage());
                    existingProduct.setDiscountPrice(product.getDiscountPrice());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setSubCategory(product.getSubCategory());
                    existingProduct.setSizes(product.getSizes());
                    existingProduct.setImage1(product.getImage1());
                    existingProduct.setImage2(product.getImage2());
                    existingProduct.setImage3(product.getImage3());
                    existingProduct.setImage4(product.getImage4());
                    existingProduct.setSeller(product.getSeller());
                    return ResponseEntity.ok(productService.saveProduct(existingProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Long productId){
        if (productService.findById(productId).isPresent()) {
            productService.deleteById(productId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/seller/{sellerId}")
    public List<Product> getProductsBySellerId(@PathVariable Long sellerId){
        return productService.findBySellerId(sellerId);
    }
}
