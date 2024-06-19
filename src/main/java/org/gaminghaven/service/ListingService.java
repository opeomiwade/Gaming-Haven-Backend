package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.requestobjects.ListingRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ListingService {

    Listing addListing(ListingRequest productRequest);

    List<Listing> getUserSoldListings();

    List<Listing> filterListings(String category,
                                 List<String> manufacturer,
                                 String condition,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice
    );


    List<Listing> sortBy(String sortBy, String categoryName, boolean increasing);
}
