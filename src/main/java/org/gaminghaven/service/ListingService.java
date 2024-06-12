package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.requestobjects.ListingRequest;

import java.util.List;

public interface ListingService {

    Listing addListing(ListingRequest productRequest);

    List<Listing> getUserSoldListings();

}
