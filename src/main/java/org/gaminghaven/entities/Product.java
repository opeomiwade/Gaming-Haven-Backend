package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private int productId;

    @Column(name = "name", nullable = false)
    private String productName;

    @Column(name = "product_type")
    private String productType;

    @Column
    private String manufacturer;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "listedProduct")
    @JsonIgnore
    private List<Listing> productListings;

    // list of users selling this product
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<User> sellers;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }

    public List<User> getSellers() {
        return sellers;
    }

    public List<Listing> getProductListings() {
        return productListings;
    }

    public void setProductListings(List<Listing> productListings) {
        this.productListings = productListings;
    }

    public void setSellers(List<User> sellers) {
        this.sellers = sellers;
    }

    public int getProductId() {
        return productId;
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

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

}
