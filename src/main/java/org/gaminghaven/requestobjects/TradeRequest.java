package org.gaminghaven.requestobjects;

import org.gaminghaven.entities.Listing;

import java.util.List;

public class TradeRequest {

    private int listingId;

    private List<Listing> offeredItems;

    public List<Listing> getOfferedItems() {
        return offeredItems;
    }

    public int getListingId() {
        return listingId;
    }
}
