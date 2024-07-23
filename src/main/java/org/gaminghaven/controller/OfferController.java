package org.gaminghaven.controller;

import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.OfferNotFound;
import org.gaminghaven.repos.OfferRepo;
import org.gaminghaven.requestobjects.OfferRequest;
import org.gaminghaven.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    OfferRepo offerRepo;

    @Autowired
    OfferService offerService;

    @GetMapping("/")
    public ResponseEntity getAllOffers() {
        return new ResponseEntity(offerRepo.findAll(), HttpStatus.OK);
    }


    @PutMapping("/{offerId}/update-status")
    public ResponseEntity acceptTrade(@PathVariable int offerId, @RequestBody Map<String, String> statusUpdate) {
        try {
            return new ResponseEntity(offerService.updateOfferStatus(offerId, statusUpdate.get("status")), HttpStatus.OK);
        } catch (OfferNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{listingId}/send-offer")
    public ResponseEntity sendOffer(@PathVariable int listingId, @RequestBody OfferRequest offerRequest) {
        try {
            return new ResponseEntity(offerService.sendOffer(listingId, offerRequest.getOffer(), offerRequest.getSenderEmail()), HttpStatus.OK);

        } catch (ListingNotFoundException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
