package com.example.ecombackend.customer.models;

import com.example.ecombackend.seller.models.Product;
import com.example.ecombackend.seller.models.Seller;
import jakarta.persistence.*;

@Entity
@Table(name = "`order`") // backticks for MySQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /* ------------  FK references (managed)  ------------ */
    private Long userId;          // who placed the order

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "seller_id")
    private Long sellerId;




    /* ------------  product snapshot  ------------ */
    private String productName;
    private Double actualPrice;
    private Double discountPercentage;
    private Double discountPrice;
    @Lob
    private String description;
    private String size;         // size chosen at checkout

    /* ------------  shipping address snapshot  ------------ */
    private String receiverName;
    private String phoneNumber;
    private String address;
    private String city;
    private String district;
    private String state;
    private String pincode;
    private String image1; // ✅ Add this field

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    /* ------------  order meta  ------------ */
    private String status = "PENDING";   // e.g. PENDING, SHIPPED, DELIVERED

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    /* --------------- getters & setters --------------- */
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }



    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getActualPrice() { return actualPrice; }
    public void setActualPrice(Double actualPrice) { this.actualPrice = actualPrice; }

    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }

    public Double getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Double discountPrice) { this.discountPrice = discountPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
