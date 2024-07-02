package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
    @Email(message = "Email is in an invalid format")
    private String email;

    @Column(length = 500, name = "password_hash")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String role;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }

    //list of user listings
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Listing> userListings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> initiatedTrades;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trade> receivedTrades;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Offer> sentOffers;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Offer> receivedOffers;

    // list of products that have been saved by a user.
    @ManyToMany
    @JoinTable(name = "saved_listings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "listing_id")})
    @JsonIgnore
    private List<Listing> savedListings;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> placedOrders;

    //list of products sold by a user
    @ManyToMany
    @JoinTable(name = "user_products",
            joinColumns = {@JoinColumn(name = "seller_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    @JsonIgnore
    private List<Product> products;

    public List<Listing> getSavedListings() {
        return savedListings;
    }

    public void setSavedListings(List<Listing> savedListings) {
        this.savedListings = savedListings;
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

    public List<Product> getProducts() {
        return products;
    }

    public List<Offer> getReceivedOffers() {
        return receivedOffers;
    }

    public List<Offer> getSentOffers() {
        return sentOffers;
    }

    public void setReceivedOffers(List<Offer> receivedOffers) {
        this.receivedOffers = receivedOffers;
    }

    public void setSentOffers(List<Offer> sentOffers) {
        this.sentOffers = sentOffers;

    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setUserListings(List<Listing> userListings) {
        this.userListings = userListings;
    }

    public int getUserId() {
        return userId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Listing> getUserListings() {
        return userListings;
    }

    public void setUserListedProducts(List<Listing> userListinggs) {
        this.userListings = userListinggs;
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

    public void setPlacedOrders(List<Order> placedOrders) {
        this.placedOrders = placedOrders;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
