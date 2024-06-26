package org.gaminghaven.service;

import org.apache.coyote.Request;
import org.gaminghaven.config.JwtService;
import org.gaminghaven.entities.Order;
import org.gaminghaven.entities.Product;
import org.gaminghaven.entities.Trade;
import org.gaminghaven.entities.User;
import org.gaminghaven.exceptions.InvalidLoginCreds;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.repos.OrderRepo;
import org.gaminghaven.repos.ProductRepo;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.Cookie;
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
    private OrderRepo tradeRepo;

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

    public User getUserByEmail(String email) throws UserNotFound {
        if (userRepo.findByEmail(email) != null) {
            return userRepo.findByEmail(email);
        } else {
            throw new UserNotFound("No user with that email exists");
        }
    }

    public Map<String, ?> getDashBoardInfo(String email) throws UserNotFound {
        HashMap<String, Object> dashboardInfo = new HashMap<>();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserNotFound("User does not exist");
        }
        BigDecimal totalExpenses = orderRepo.getUserTotalExpenses(user.getUserId()) == null ? new BigDecimal("0.00") : orderRepo.getUserTotalExpenses(user.getUserId());
        BigDecimal totalIncome = orderRepo.getUserTotalIncome(user.getUserId()) == null ? new BigDecimal("0.00") : orderRepo.getUserTotalIncome(user.getUserId());
        BigDecimal netIncome = totalIncome.subtract(totalExpenses);
        BigDecimal totalSales = orderRepo.calculateTotalSales();
        List<Trade> receivedTrades = user.getReceivedTrades();
        List<Trade> sentTrades = user.getInitiatedTrades();
        List<Product> savedItems = user.getSavedItems();
        List<Product> userListedProducts = user.getUserListedProducts();
        List<Order> placedOrders = user.getPlacedOrders();
        List<Order> receivedOrders = user.getReceivedOrders();
        dashboardInfo.put("totalExpenses", totalExpenses);
        dashboardInfo.put("totalIncome", totalIncome);
        dashboardInfo.put("netIncome", netIncome);
        dashboardInfo.put("totalSales", totalSales);
        dashboardInfo.put("receivedTrades", receivedTrades);
        dashboardInfo.put("sentTrades", sentTrades);
        dashboardInfo.put("savedItems", savedItems);
        dashboardInfo.put("listedProducts", userListedProducts);
        dashboardInfo.put("placedOrders", placedOrders);
        dashboardInfo.put("receivedOrders", receivedOrders);
        return dashboardInfo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}


