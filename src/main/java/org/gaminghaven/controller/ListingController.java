package org.gaminghaven.controller;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.repos.ListingRepo;
import org.gaminghaven.requestobjects.ListingRequest;
import org.gaminghaven.service.ListingService;
import org.gaminghaven.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    private ListingRepo listingRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    ListingService listingService;

    @GetMapping("/")
    public ResponseEntity getAllListings() {
        return new ResponseEntity(listingRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity getListingsByCategory(@PathVariable String category) {
        List<Listing> products = listingRepo.findByCategoryName(category);
        if (products.size() < 1) {
            return new ResponseEntity("No listings found for that category", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity addListing(@RequestBody ListingRequest listingRequest) {
        productService.addProduct(listingRequest);
        listingService.addListing(listingRequest);
        return new ResponseEntity("Listing posted sucessfully", HttpStatus.OK);
    }
}
