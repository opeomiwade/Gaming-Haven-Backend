package org.gaminghaven.requestobjects;

import org.gaminghaven.entities.ListingImage;

import java.math.BigDecimal;
import java.util.List;

public class ListingRequest {
    private String productName;

    private String categoryName;

    private String condition;

    private String description;

    private BigDecimal price;

    private String[] imageUrls;

    private List<ListingImage> images;

    private String manufacturer;

    private List<Integer> deleteImages;

    public BigDecimal getPrice() {
        return price;
    }

    public List<Integer> getDeleteImages() {
        return deleteImages;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public List<ListingImage> getImages() {
        return images;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
