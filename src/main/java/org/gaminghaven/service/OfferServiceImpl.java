package org.gaminghaven.service;
import org.gaminghaven.entities.Listing;
import org.gaminghaven.entities.Offer;
import org.gaminghaven.entities.User;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.OfferNotFound;
import org.gaminghaven.repos.ListingRepo;
import org.gaminghaven.repos.OfferRepo;
import org.gaminghaven.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    OfferRepo offerRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ListingRepo listingRepo;

    @Override
    public Offer updateOfferStatus(int tradeId, String status) throws OfferNotFound {
        Offer offer = offerRepo.findById(tradeId).orElseThrow(() -> new OfferNotFound("No trade with that id"));
        offer.setStatus(status);
        offerRepo.save(offer);
        return offer;
    }
    @Override
    public Offer sendOffer(int listingId, String offer,
                           String senderEmail) throws ListingNotFoundException {
        Listing listing = listingRepo.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException(("No listing with that id was found")));
        User sender = userRepo.findByEmail(senderEmail);
        Offer newOffer = new Offer();
        newOffer.setOffer(new BigDecimal(offer));
        newOffer.setListing(listing);
        newOffer.setSender(sender);
        newOffer.setRecipient(listing.getSeller());
        offerRepo.save(newOffer);
        List<Offer> offers = listing.getOffers();
        offers.add(newOffer);
        listing.setOffers(offers);
        listingRepo.save(listing);
        return newOffer;
    }


}
