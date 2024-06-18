package org.gaminghaven.controller;

import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.requestobjects.LoginRequest;
import org.gaminghaven.entities.Trade;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.entities.User;
import org.gaminghaven.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl service;

    @GetMapping("/")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(service.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody User user) {
        try {
            return new ResponseEntity<>(service.createNewUser(user), HttpStatus.CREATED);
        } catch (UserNotFound exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        try {
            return new ResponseEntity(service.loginUser(loginRequest.getEmail(), loginRequest.getPassword(), response, request), HttpStatus.OK);
        } catch (AuthenticationException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return new ResponseEntity(service.getUserByEmail(email), HttpStatus.OK);
        } catch (UserNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email) {
        try {
            return new ResponseEntity(service.getUserByEmail(email), HttpStatus.OK);
        } catch (UserNotFound exception) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dashboard-details")
    public ResponseEntity getDetails(HttpServletResponse response, HttpServletRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return new ResponseEntity(service.getDashBoardInfo(email), HttpStatus.OK);
        } catch (UserNotFound exception) {
            return new ResponseEntity("User does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/trades")
    public ResponseEntity getAllTrades() {
        User user;
        Map<String, Object> trades;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            user = service.getUserByEmail(email);
            trades = new HashMap<>();
            List<Trade> initiatedTrades = user.getInitiatedTrades();
            List<Trade> receivedTrades = user.getReceivedTrades();
            trades.put("initiatedTrades", initiatedTrades);
            trades.put("receivedTrades", receivedTrades);
        } catch (UserNotFound exception) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(trades, HttpStatus.OK);
    }

    @GetMapping("/saved-listings")
    public ResponseEntity getSavedListings() {
        User user;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            user = service.getUserByEmail(email);
        } catch (UserNotFound exception) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user.getSavedListings(), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user-listings")
    public ResponseEntity getUserListedProducts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        try {
            user = service.getUserByEmail(email);
        } catch (UserNotFound exception) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user.getUserListings(), HttpStatus.OK);
    }

    @GetMapping("/user/purchases")
    public ResponseEntity getPurchases() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        try {
            user = service.getUserByEmail(email);
            return new ResponseEntity(user.getPlacedOrders(), HttpStatus.OK);
        } catch (UserNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-saved-listing")
    public ResponseEntity addSavedListing(@RequestParam int listingId) {
        try {
            service.addSavedListing(listingId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserNotFound | ListingNotFoundException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);

        }
    }

    @DeleteMapping("/remove-saved-listing")
    public ResponseEntity removeSavedListing(@RequestParam int listingId) {
        try {
            service.removeSavedListing(listingId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ListingNotFoundException | UserNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
