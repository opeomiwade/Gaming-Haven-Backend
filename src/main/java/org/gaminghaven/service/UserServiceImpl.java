package org.gaminghaven.service;

import org.gaminghaven.config.JwtService;
import org.gaminghaven.entities.*;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private TradeRepo tradeRepo;

    @Autowired
    private ListingRepo listingRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    private LocalDateTime currentTimeStamp;

    public UserServiceImpl() {
        this.currentTimeStamp = LocalDateTime.now();
    }

    @Override
    public Map<String, String> createNewUser(User user) throws UserNotFound {
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            throw new UserNotFound("Please fill in all details");
        } else {
            HashMap<String, String> responseBody = new HashMap<>();
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRole("USER");
            user.setCreatedAt(currentTimeStamp);
            user.setUpdatedAt(currentTimeStamp);

            // add user to the database.
            try {
                userRepo.save(user);
                var jwtToken = jwtService.generateToken(user.getEmail());
                responseBody.put("message", "User created successfully");
                responseBody.put("token", jwtToken);
            } catch (Exception exception) {
                throw new UserNotFound("A user with that email or username exists, try again");
            }
            return responseBody;
        }
    }

    @Override
    public Map<String, String> loginUser(String email, String password, HttpServletResponse response, HttpServletRequest request) throws AuthenticationException {
        HashMap<String, String> responseBody = new HashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, password));
            String idToken = jwtService.generateToken(email);
            responseBody.put("accessToken", idToken);
            responseBody.put("message", "user logged in successfully");
            return responseBody;
        } catch (AuthenticationException exception) {
            throw exception;
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFound {
        if (userRepo.findByEmail(email) != null) {
            return userRepo.findByEmail(email);
        } else {
            throw new UserNotFound("No user with that email exists");
        }
    }

    @Override
    public Map<String, ?> getDashBoardInfo(String email) throws UserNotFound {
        HashMap<String, Object> dashboardInfo = new HashMap<>();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFound("User does not exist");
        }
        BigDecimal totalExpenses = orderRepo.getUserTotalExpenses(user.getUserId()) == null ? new BigDecimal("0.00") : orderRepo.getUserTotalExpenses(user.getUserId());
        BigDecimal totalIncome = listingRepo.getUserTotalIncome(user.getUserId()) == null ? new BigDecimal("0.00") : listingRepo.getUserTotalIncome(user.getUserId());
        BigDecimal netIncome = totalIncome.subtract(totalExpenses);
        BigDecimal totalSales = orderRepo.calculateTotalSales();
        List<Trade> receivedTrades = user.getReceivedTrades();
        List<Trade> sentTrades = user.getInitiatedTrades();
        List<Listing> savedListings = user.getSavedListings();
        List<Listing> userListedProducts = user.getUserListings();
        List<Order> orders = user.getPlacedOrders();
        List<Listing> soldListings = listingRepo.getUserSoldListings(user.getUserId());
        List<Offer> sentOffers = user.getSentOffers();
        List<Offer> receivedOffers = user.getReceivedOffers();
        dashboardInfo.put("totalExpenses", totalExpenses);
        dashboardInfo.put("totalIncome", totalIncome);
        dashboardInfo.put("netIncome", netIncome);
        dashboardInfo.put("marketplaceTotalSales", totalSales);
        dashboardInfo.put("receivedTrades", receivedTrades);
        dashboardInfo.put("sentTrades", sentTrades);
        dashboardInfo.put("savedListings", savedListings);
        dashboardInfo.put("listedProducts", userListedProducts);
        dashboardInfo.put("orders", orders);
        dashboardInfo.put("soldListings", soldListings);
        dashboardInfo.put("sentOffers", sentOffers);
        dashboardInfo.put("receivedOffers", receivedOffers);
        return dashboardInfo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void addSavedListing(int listingId) throws UserNotFound, ListingNotFoundException {
        Listing listing = listingRepo.findById(listingId).orElseThrow(() -> new ListingNotFoundException("listing not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByEmail(email);
        user.getSavedListings().add(listing);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public void removeSavedListing(int listingId) throws UserNotFound, ListingNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByEmail(email);
        Listing listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found"));
        user.getSavedListings().remove(listing);
        userRepo.save(user);
    }
}


