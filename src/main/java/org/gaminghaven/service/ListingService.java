package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.requestobjects.ListingRequest;

public interface ListingService {

    Listing addListing(ListingRequest productRequest);

}
