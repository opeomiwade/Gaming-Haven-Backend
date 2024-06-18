package org.gaminghaven.service;

import org.gaminghaven.entities.Category;
import org.gaminghaven.entities.Product;
import org.gaminghaven.entities.User;
import org.gaminghaven.repos.CategoryRepo;
import org.gaminghaven.repos.ListingImageRepo;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.requestobjects.ListingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ListingImageRepo listingImageRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    @Transactional
    public Product addProduct(ListingRequest listingRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        Product product = null;
        if (productRepo.findByProductName(listingRequest.getProductName()) == null) {
            product = new Product();
            product.setProductName(listingRequest.getProductName());
            product.setProductType(listingRequest.getProductType());
            product.setManufacturer(listingRequest.getManufacturer());
            Category category = categoryRepo.findByName(listingRequest.getProductType());
            product.setCategory(category);
            productRepo.save(product);
        } else {
            product = productRepo.findByProductName(listingRequest.getProductName());
        }
        user.getProducts().add(product);
        return product;
    }

    @Override
    public List<String> getManufacturers() {
        return productRepo.getManufacturers();
    }
}
