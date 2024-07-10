package org.gaminghaven.service;

import org.gaminghaven.entities.Category;
import org.gaminghaven.entities.Product;
import org.gaminghaven.entities.User;
import org.gaminghaven.exceptions.PersistenceException;
import org.gaminghaven.exceptions.ProductNotFound;
import org.gaminghaven.repos.CategoryRepo;
import org.gaminghaven.repos.ListingImageRepo;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.requestobjects.ListingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    public Product addProduct(ListingRequest listingRequest) throws PersistenceException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        Product product = null;

        if (listingRequest.getProductName() == null ||
                listingRequest.getCategoryName() == null ||
                listingRequest.getManufacturer() == null ||
                listingRequest.getCondition() == null ||
                listingRequest.getDescription() == null |
                        listingRequest.getImageUrls() == null ||
                listingRequest.getPrice() == null) {
            throw new PersistenceException("Please fill in all details");
        }
        if (productRepo.findByProductName(listingRequest.getProductName()) == null) {
            product = new Product();
            product.setProductName(listingRequest.getProductName());
            product.setProductType(listingRequest.getCategoryName());
            product.setManufacturer(listingRequest.getManufacturer());
            Category category = categoryRepo.findByName(listingRequest.getCategoryName());
            product.setCategory(category);
            productRepo.save(product);
        } else {
            product = productRepo.findByProductName(listingRequest.getProductName());
        }
        //prevents duplicates entries in table,ensures composite primary key constraint is maintained
        if (!user.getProducts().contains(product)) user.getProducts().add(product);
        return product;
    }

    @Override
    public Product updateListedProduct(String productName, String categoryName, String manufacturer) {
        Product product = productRepo.findByProductName(productName);
        boolean edited = false;

        if (product == null) {
            Product newProduct = new Product();
            if (manufacturer != null) newProduct.setManufacturer(manufacturer);
            if (productName != null) newProduct.setProductName(productName);
            if (categoryName != null) newProduct.setCategory(categoryRepo.findByName(categoryName));
            productRepo.save(newProduct);
            return newProduct;
        }

        if (categoryName != null && !categoryName.equals(product.getCategory().getName())) {
            Category updatedCategory = categoryRepo.findByName(categoryName);
            product.setCategory(updatedCategory);
            edited = true;
        }

        if (manufacturer != null && !manufacturer.equals(product.getManufacturer())) {
            product.setManufacturer(manufacturer);
            edited = true;
        }

        if (edited) {
            product.setUpdatedAt(LocalDateTime.now());
            productRepo.save(product);
        }
        return product;
    }

    @Override
    public List<String> getManufacturers() {
        return productRepo.getManufacturers();
    }

    @Override
    public Product getProductById(int productId) throws ProductNotFound {
        return productRepo.findById(productId).
                orElseThrow(() -> new ProductNotFound("Product with that id does not exist"));
    }

    @Override
    public List<Product> filterProducts(String categoryName, String productName, String manufacturer) {
        Specification spec = Specification.where(null);

        if (categoryName != null)
            spec = spec.and(((root, query, cb) ->
                cb.equal(cb.lower(root.get("category").get("name")), categoryName.toLowerCase())));

        if(productName != null)
            spec = spec.and(((root, query, cb) ->
                cb.equal(cb.lower(root.get("productName")), productName.toLowerCase())));


        if(manufacturer != null)
            spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("manufacturer")),
                        manufacturer.toLowerCase()));

        return productRepo.findAll(spec);
    }
}
