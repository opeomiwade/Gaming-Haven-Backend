package org.gaminghaven.requestobjects;

import java.math.BigDecimal;

public class ListingRequest {
    private String productName;

    private String productType;

    private String condition;

    private String description;

    private BigDecimal price;

    private String[] imageUrls;

    private String manufacturer;

    public BigDecimal getPrice() {
        return price;
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

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
