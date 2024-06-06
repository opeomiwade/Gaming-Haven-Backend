package org.gaminghaven.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(name = "order_listings",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "listing_id")})
    private List<Listing> listing;

    public List<Listing> getListing() {
        return listing;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setListing(List<Listing> listing) {
        this.listing = listing;
    }

    public int getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public User getBuyerId() {
        return buyer;
    }

    public void setBuyerId(User buyer) {
        this.buyer = buyer;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public User getBuyer() {
        return buyer;
    }
}
