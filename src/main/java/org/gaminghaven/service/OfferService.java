package org.gaminghaven.service;

import org.gaminghaven.entities.Offer;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.OfferNotFound;

public interface OfferService {
    Offer updateOfferStatus(int tradeId, String status) throws OfferNotFound;

    Offer sendOffer(int listingId, String offer,
                    String senderEmail) throws ListingNotFoundException;

}
