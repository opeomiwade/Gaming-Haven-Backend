package org.gaminghaven.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column(nullable = false)
    private BigDecimal offer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private String status;

    @PrePersist
    protected void onCreate() {
        status = "sent";
        createdAt = LocalDateTime.now();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public Listing getListing() {
        return listing;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setOffer(BigDecimal offer) {
        this.offer = offer;
    }

    public BigDecimal getOffer() {
        return offer.setScale(2, RoundingMode.HALF_UP);
    }

    public User getSender() {
        return sender;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
