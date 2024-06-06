package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.entities.ListingImage;
import org.gaminghaven.entities.Product;
import org.gaminghaven.entities.User;
import org.gaminghaven.repos.ListingImageRepo;
import org.gaminghaven.repos.ListingRepo;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.requestobjects.ListingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListingServiceImpl implements ListingService {

    @Autowired
    ListingRepo listingRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ListingImageRepo listingImageRepo;

    @Autowired
    ProductRepo productRepo;

    @Override
    @Transactional
    public Listing addListing(ListingRequest productRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        //get currently logged in user for new listing
        User user = userRepo.findByEmail(email);

        // find product associated with the new listing
        Product product = productRepo.findByProductName(productRequest.getProductName());

        Listing listing = new Listing();
        listing.setListedProduct(product);
        listing.setCondition(productRequest.getCondition());
        listing.setDescription(productRequest.getDescription());
        listing.setPrice(productRequest.getPrice());
        listing.setSeller(user);
        listingRepo.save(listing);

        // add all images associated with this listing
        List<ListingImage> images = new ArrayList<>();
        ListingImage listingImage = new ListingImage();
        for (String imageUrl : productRequest.getImageUrls()) {
            listingImage.setImageUrl(imageUrl);
            listingImage.setListing(listing);
            listingImageRepo.save(listingImage);
            images.add(listingImage);
        }
        listing.setImages(images);
        return listing;
    }
}
