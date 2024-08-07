package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.exceptions.ImageNotFound;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.ProductNotFound;
import org.gaminghaven.requestobjects.ListingRequest;
import org.gaminghaven.entities.Offer;

import java.math.BigDecimal;
import java.util.List;

public interface ListingService {

    Listing addListing(ListingRequest productRequest);

    List<Listing> getUserSoldListings();

    List<Listing> filterListings(String category,
                                 List<String> manufacturer,
                                 List<String> condition,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice,
                                 String sortBy,
                                 boolean increasing
    );

    List<Listing> sortBy(String sortBy, String categoryName, boolean increasing);

    Listing editListing(int listingId, ListingRequest listingRequest) throws ProductNotFound, ImageNotFound;

    void deleteListing(int listingId) throws ListingNotFoundException;

    List<Offer> getListingOffers(int listingId) throws ListingNotFoundException;
}
