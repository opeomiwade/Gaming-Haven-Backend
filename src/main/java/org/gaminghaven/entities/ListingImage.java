package org.gaminghaven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "product_listing_images")
public class ListingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int imageId;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    @JsonIgnore
    private Listing listing;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Listing getListing() {
        return listing;
    }

    public int getImageId() {
        return imageId;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }
}
