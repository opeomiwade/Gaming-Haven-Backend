package org.gaminghaven.service;

import org.gaminghaven.entities.Product;
import org.gaminghaven.exceptions.PersistenceException;
import org.gaminghaven.requestobjects.ListingRequest;

import java.util.List;

public interface ProductService {

    /**
     * add new product to the table if it doesnt already exist in database.
     * @param productRequest
     * @return product - the added product.
     */
    Product addProduct(ListingRequest productRequest) throws PersistenceException;

    Product updateListedProduct(String productName, String categoryName, String manufacturer);

    List<String> getManufacturers();
}