package org.gaminghaven.controller;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
