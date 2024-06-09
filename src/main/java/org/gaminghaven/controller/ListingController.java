package org.gaminghaven.controller;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.entities.User;
import org.gaminghaven.repos.ListingRepo;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.requestobjects.ListingRequest;
import org.gaminghaven.service.ListingService;
import org.gaminghaven.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepo userRepo;

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

    @GetMapping("/user-listings")
    public ResponseEntity getUserListings() {
        User seller = userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Listing> userListings = listingRepo.findBySeller(seller);
        System.out.println(userListings);
        if (userListings.size() < 1) {
            return new ResponseEntity("No listings for this user", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userListings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getListingById(@PathVariable int id) {
        Listing listing = listingRepo.findById(id).orElse(null);
        System.out.println(listing);
        if (listing == null) {
            return new ResponseEntity("Listing was not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(listing, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity addListing(@RequestBody ListingRequest listingRequest) {
        productService.addProduct(listingRequest);
        listingService.addListing(listingRequest);
        return new ResponseEntity("Listing posted sucessfully", HttpStatus.OK);
    }
}
