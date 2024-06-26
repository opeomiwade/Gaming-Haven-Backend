package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 500, name = "password_hash")
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private  String role;

    //list of products sold by a user
    @ManyToMany
    @JoinTable(name = "user_products",
            joinColumns = {@JoinColumn(name = "seller_id")},
            inverseJoinColumns = {@JoinColumn(name="product_id")})
    @JsonIgnore
    private List<Product> userListedProducts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> initiatedTrades;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> receivedTrades;

    // list of products that have been saved by a user.
    @ManyToMany
    @JoinTable(name = "saved_items",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    @JsonIgnore
    private List<Product> savedItems;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> placedOrders;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> receivedOrders;

    public List<Product> getSavedItems() {
        return savedItems;
    }

    public void setSavedItems(List<Product> savedItems) {
        this.savedItems = savedItems;
    }

    public void setReceivedTrades(List<Trade> receivedTrades) {
        this.receivedTrades = receivedTrades;
    }

    public void setInitiatedTrades(List<Trade> initiatedTrades) {
        this.initiatedTrades = initiatedTrades;
    }

    public List<Trade> getReceivedTrades() {
        return receivedTrades;
    }

    public List<Trade> getInitiatedTrades() {
        return initiatedTrades;
    }

    public int getUserId() {
        return userId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Product> getUserListedProducts() {
        return userListedProducts;
    }

    public void setUserListedProducts(List<Product> userListedProducts) {
        this.userListedProducts = userListedProducts;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Order> getPlacedOrders() {
        return placedOrders;
    }

    public List<Order> getReceivedOrders() {
        return receivedOrders;
    }

    public void setPlacedOrders(List<Order> placedOrders) {
        this.placedOrders = placedOrders;
    }

    public void setReceivedOrders(List<Order> receivedOrders) {
        this.receivedOrders = receivedOrders;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
