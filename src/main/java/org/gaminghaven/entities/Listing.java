package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listingId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product listedProduct;

    //images for this listing
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private List<ListingImage> images;

    // list of cash offers for this listing
    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Offer> offers;

    //order associated with this listing.
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToMany(mappedBy = "savedListings")
    private List<User> savedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "requestedItem")
    private List<Trade> trades;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    @Column(nullable = false)
    private String condition;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private String status;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        status = "available";
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<ListingImage> getImages() {
        return images;
    }

    public void setImages(List<ListingImage> images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public int getListingId() {
        return listingId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Product getListedProduct() {
        return listedProduct;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getSeller() {
        return seller;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setListedProduct(Product listedProduct) {
        this.listedProduct = listedProduct;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


