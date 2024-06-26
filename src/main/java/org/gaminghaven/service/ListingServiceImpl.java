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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import javax.transaction.Transactional;
import java.math.BigDecimal;
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
        for (String imageUrl : productRequest.getImageUrls()) {
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
                                        String condition,
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

        if (condition != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.equal(cb.lower(root.get("condition")), condition.toLowerCase())));
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
}
