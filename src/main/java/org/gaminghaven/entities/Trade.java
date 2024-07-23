package org.gaminghaven.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "trade_offered_listings",
            joinColumns = {@JoinColumn(name = "trade_id")},
            inverseJoinColumns = {@JoinColumn(name = "listing_id")} )
    private List<Listing> offeredItems;

    @ManyToOne
    @JoinColumn(name = "recipient", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "requested_item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing requestedItem;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "trade_status")
    private String tradeStatus;

    @PrePersist
    protected void onCreate() {
        tradeStatus = "pending";
        createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public List<Listing> getOfferedItems() {
        return offeredItems;
    }

    public void setOfferedItems(List<Listing> offeredItems) {
        this.offeredItems = offeredItems;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Listing getRequestedItem() {
        return requestedItem;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRequestedItem(Listing requestedItem) {
        this.requestedItem = requestedItem;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
