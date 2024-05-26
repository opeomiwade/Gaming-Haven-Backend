package org.gaminghaven.entities;

import javax.persistence.*;

@Entity(name = "trades")
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
    @JoinColumn(name = "product_id_1", nullable = false)
    private Product product1;

    @ManyToOne
    @JoinColumn(name = "product_id_2", nullable = false)
    private Product product2;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Product getProduct1() {
        return product1;
    }

    public Product getProduct2() {
        return product2;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setProduct1(Product product1) {
        this.product1 = product1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setProduct2(Product product2) {
        this.product2 = product2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
