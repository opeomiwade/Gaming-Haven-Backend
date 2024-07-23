package org.gaminghaven.service;

import org.gaminghaven.entities.*;
import org.gaminghaven.exceptions.ImageNotFound;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.ProductNotFound;
import org.gaminghaven.repos.*;
import org.gaminghaven.requestobjects.ListingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    OfferRepo offerRepo;


    @Override
    @Transactional
    public Listing addListing(ListingRequest listingRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        //get currently logged in user for new listing
        User user = userRepo.findByEmail(email);

        // find product associated with the new listing
        Product product = productRepo.findByProductName(listingRequest.getProductName());

        Listing listing = new Listing();
        listing.setListedProduct(product);
        listing.setCondition(listingRequest.getCondition());
        listing.setDescription(listingRequest.getDescription());
        listing.setPrice(listingRequest.getPrice());
        listing.setSeller(user);
        listingRepo.save(listing);

        // add all images associated with this listing
        List<ListingImage> images = new ArrayList<>();
        for (String imageUrl : listingRequest.getImageUrls()) {
            ListingImage listingImage = new ListingImage();
            listingImage.setImageUrl(imageUrl);
            listingImage.setListing(listing);
            listingImageRepo.save(listingImage);
            images.add(listingImage);
        }
        listing.setImages(images);
        return listing;
    }

    @Override
    public List<Listing> getUserSoldListings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        //get currently logged-in user for new listing
        User user = userRepo.findByEmail(email);
        List<Listing> soldListings = listingRepo.getUserSoldListings(user.getUserId());
        return soldListings;

    }

    @Override
    public List<Listing> filterListings(String categoryName,
                                        List<String> manufacturers,
                                        List<String> condition,
                                        BigDecimal minPrice,
                                        BigDecimal maxPrice,
                                        String sortBy,
                                        boolean increasing) {
        Specification<Listing> spec = Specification.where(null);
        if (categoryName != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.equal(cb.lower(root.get("listedProduct").get("category").get("name")),
                            categoryName.toLowerCase())));
        }

        if (condition != null && condition.size() == 1) {
            spec = spec.and(((root, query, cb) ->
                    cb.equal(cb.lower(root.get("condition")), condition.get(0).toLowerCase())));
        }

        if (condition != null && condition.size() > 1) {

            // create null specification object for multiple conditions situation
            Specification<Listing> conditionsSpec = Specification.where(null);

            for (String c : condition) {
                // build spec object by checking if the condition in the listing is
                // equal to either condition in the condition list
                conditionsSpec = conditionsSpec.or((root, query, cb) ->
                        cb.equal(cb.lower(root.get("condition")),
                                c.toLowerCase()));

            }
            spec = spec.and(conditionsSpec);
        }

        if (manufacturers != null && manufacturers.size() == 1) {
            spec = spec.and(((root, query, cb) ->
                    cb.equal(cb.lower(root.get("listedProduct").get("manufacturer")), manufacturers.get(0).toLowerCase())));
        }

        if (manufacturers != null && manufacturers.size() > 1) {

            // create null specification object for multiple manufacturer situation
            Specification<Listing> manufacturersSpec = Specification.where(null);

            for (String manufacturer : manufacturers) {
                // build spec object by checking if the manufacturer in the listing is
                // equal to either manufacturer in the manufacturer list
                manufacturersSpec = manufacturersSpec.or((root, query, cb) ->
                        cb.equal(cb.lower(root.get("listedProduct").get("manufacturer")),
                                manufacturer.toLowerCase()));

            }
            spec = spec.and(manufacturersSpec);
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.between(root.get("price"), minPrice, maxPrice)));
        }

        if (minPrice == null && maxPrice != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
        }

        if (maxPrice == null && minPrice != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice)));
        }

        if (sortBy != null) {
            Sort sort = Sort.by(increasing ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            return listingRepo.findAll(spec, sort);
        }
        return listingRepo.findAll(spec);
    }

    @Override
    public List<Listing> sortBy(String sortBy, String categoryName, boolean increasing) {
        Specification<Listing> spec = Specification.where(null);
        if (increasing) {
            spec = spec.and(((root, query, cb) -> {
                query.orderBy(cb.asc(root.get(sortBy)));
                return categoryName == null ? cb.conjunction() :
                        cb.equal(cb.lower(root.get("listedProduct").get("category").get("name")), categoryName.toLowerCase());
            }
            ));
        } else {
            spec = spec.and(((root, query, cb) -> {
                query.orderBy(cb.desc(root.get(sortBy)));
                return categoryName == null ? cb.conjunction() :
                        cb.equal(cb.lower(root.get("listedProduct").get("category").get("name")), categoryName.toLowerCase());
            }
            ));
        }
        return listingRepo.findAll(spec);
    }

    @Override
    public Listing editListing(int listingId, ListingRequest listingRequest) throws ProductNotFound, ImageNotFound {
        Listing listingToEdit = listingRepo.findById(listingId).
                orElseThrow(() -> new ProductNotFound("No Listing with that Id was found"));

        if (listingRequest.getPrice() != null &&
                !listingRequest.getPrice().equals(listingToEdit.getPrice())) {
            listingToEdit.setPrice(listingRequest.getPrice());
        }

        if (listingRequest.getCondition() != null &&
                !listingRequest.getCondition().equals(listingToEdit.getCondition())) {
            listingToEdit.setCondition(listingRequest.getCondition());
        }

        if (listingRequest.getDescription() != null &&
                !listingRequest.getDescription().equals(listingToEdit.getDescription())) {
            listingToEdit.setDescription(listingRequest.getDescription());
        }

        if (listingRequest.getImageUrls() != null) {
            List<ListingImage> updatedImages = listingToEdit.getImages();
            for (String imageUrl : listingRequest.getImageUrls()) {
                // save new images user added to listing
                ListingImage newImage = new ListingImage();
                newImage.setImageUrl(imageUrl);
                newImage.setListing(listingToEdit);
                listingImageRepo.save(newImage);
                updatedImages.add(newImage);
            }
            listingToEdit.setImages(updatedImages);
        }

        if (listingRequest.getDeleteImages() != null && listingRequest.getDeleteImages().size() > 0) {
            for (int id : listingRequest.getDeleteImages()) {
                deleteListingImage(id);
            }
        }
        listingToEdit.setUpdatedAt(LocalDateTime.now());
        listingRepo.save(listingToEdit);
        return listingToEdit;
    }

    @Override
    public void deleteListing(int listingId) throws ListingNotFoundException {
        Listing listingToDelete = listingRepo.findById(listingId).
                orElseThrow(() -> new ListingNotFoundException("No listing with that id exists"));
        listingRepo.delete(listingToDelete);
    }

    @Override
    public List<Offer> getListingOffers(int listingId) throws ListingNotFoundException {
        Listing listing = listingRepo.findById(listingId).orElseThrow(() ->
                new ListingNotFoundException("No listing with that Id was found"));
        return listing.getOffers();
    }

    private void deleteListingImage(int id) throws ImageNotFound {
        ListingImage imageToDelete = listingImageRepo.findById(id).
                orElseThrow(() -> new ImageNotFound("No Image with that id exists"));
        listingImageRepo.delete(imageToDelete);
    }
}
