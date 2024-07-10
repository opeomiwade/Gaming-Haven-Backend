package org.gaminghaven.controller;

import org.gaminghaven.exceptions.ProductNotFound;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductServiceImpl service;

    @Autowired
    ProductRepo productRepo;

    @GetMapping("/")
    public ResponseEntity getAllProducts() {
        return new ResponseEntity(productRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/manufacturers")
    public ResponseEntity getManufacturers() {
        return new ResponseEntity(service.getManufacturers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable int id) {
        try {
            return new ResponseEntity(service.getProductById(id), HttpStatus.OK);
        } catch (ProductNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity filterProducts(@RequestParam(required = false) String category,
                                         @RequestParam(required = false) String manufacturer,
                                         @RequestParam(required = false) String name) {
      return new ResponseEntity(service.
              filterProducts(category, name, manufacturer), HttpStatus.OK);
    }


}
