package org.gaminghaven.controller;

import org.gaminghaven.exceptions.OrderNotFoundException;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.repos.OrderRepo;
import org.gaminghaven.service.OrderService;
import org.gaminghaven.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl service;

    @GetMapping("/")
    public ResponseEntity getAllOrders() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderById(@PathVariable int id) {
        try {
            return new ResponseEntity(service.getOrderById(id), HttpStatus.OK);
        } catch (OrderNotFoundException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
