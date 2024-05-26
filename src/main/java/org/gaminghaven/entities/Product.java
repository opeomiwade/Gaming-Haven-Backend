package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false)
    private String productName;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column
    private String manufacturer;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @Column
    private String condition;

    @Column(name = "sold_by_us" , nullable = false)
    private Boolean soldByUs;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private List<ProductImage> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @JsonIgnore
    private List<Review> reviews;

    // list of users selling this product
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<User> users;


    @OneToMany(mappedBy = "product1", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> initiatedTrades;

    @OneToMany(mappedBy = "product2", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> receivedTrades;

    public List<Review> getReviews() {
        return reviews;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Trade> getInitiatedTrades() {
        return initiatedTrades;
    }

    public List<Trade> getReceivedTrades() {
        return receivedTrades;
    }

    public void setInitiatedTrades(List<Trade> initiatedTrades) {
        this.initiatedTrades = initiatedTrades;
    }

    public void setReceivedTrades(List<Trade> receivedTrades) {
        this.receivedTrades = receivedTrades;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getProductId() {
        return productId;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getSoldByUs() {
        return soldByUs;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setSoldByUs(Boolean soldByUs) {
        this.soldByUs = soldByUs;
    }
}
