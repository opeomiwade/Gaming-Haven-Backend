package org.gaminghaven.controller;

import org.gaminghaven.exceptions.InvalidLoginCreds;
import org.gaminghaven.exceptions.UserNotFound;
import org.gaminghaven.entities.User;
import org.gaminghaven.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl service;

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody User user) {
        try {
            return new ResponseEntity<>(service.createNewUser(user), HttpStatus.CREATED);
        } catch (UserNotFound exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        try {
            return new ResponseEntity(service.loginUser(user.getEmail(), user.getPassword()), HttpStatus.OK);
        } catch (InvalidLoginCreds exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/{email}")
    public ResponseEntity getUser(@PathVariable String email) {
        try {
            return new ResponseEntity(service.getUserByEmail(email), HttpStatus.OK);
        } catch (UserNotFound exception) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
    }


}
