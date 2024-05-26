package org.gaminghaven.service;

import org.gaminghaven.entities.User;
import org.gaminghaven.exceptions.InvalidLoginCreds;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.repos.UserRepo;
import org.gaminghaven.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private SCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;
    private LocalDateTime currentTimeStamp;

    public UserServiceImpl() {
        this.encoder = SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8();
        this.currentTimeStamp = LocalDateTime.now();
    }

    @Override
    public Map<String, String> createNewUser(User user) throws UserNotFound {
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            throw new UserNotFound("Please fill in all details");
        } else {
            HashMap<String, String> responseBody = new HashMap<>();
            String hashedPassword = encoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setCreatedAt(currentTimeStamp);
            user.setUpdatedAt(currentTimeStamp);
            String accessToken = jwtUtil.generateToken(user.getUsername());
            responseBody.put("accessToken", accessToken);
            responseBody.put("message", "User created successfully");
            // add user to the database.
            try {
                userRepo.save(user);
            } catch (Exception exception) {
                throw new UserNotFound("A user with that email or username exists, try again");
            }
            return responseBody;
        }
    }

    public HashMap<String, String> loginUser(String email, String password) throws InvalidLoginCreds {
        User user = null;
        if (userRepo.findByEmail(email) == null) {
            throw new InvalidLoginCreds("User with those details do not exist");
        } else {
            user = userRepo.findByEmail(email);
        }

        HashMap<String, String> responseBody = new HashMap<>();
        if (encoder.matches(password, user.getPassword())) {
            String accessToken = jwtUtil.generateToken(user.getEmail());
            responseBody.put("accessToken", accessToken);
            responseBody.put("message", "User logged in successfully");
        } else {
            throw new InvalidLoginCreds("Invalid login credentials, email and password dont match");
        }
        return responseBody;
    }

    public User getUserByEmail(String email) throws UserNotFound {
        if (userRepo.findByEmail(email) != null) {
            return userRepo.findByEmail(email);
        } else {
            throw new UserNotFound("No user with that email exists");

        }

    }

}


