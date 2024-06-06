package org.gaminghaven.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    @ManyToOne
    @JoinColumn(name = "listing_id_1", nullable = false)
    private Listing listing1;

    @ManyToOne
    @JoinColumn(name = "listing_id_2", nullable = false)
    private Listing listing2;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Listing getListing1() {
        return listing1;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Listing getListing2() {
        return listing2;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setListing1(Listing listing1) {
        this.listing1 = listing1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setListing2(Listing listing2) {
        this.listing2 = listing2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
