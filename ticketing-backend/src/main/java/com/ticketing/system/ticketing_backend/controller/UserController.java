package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.User;
import com.ticketing.system.ticketing_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user registration and login operations.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from frontend
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user.
     * @param user the user to be registered
     * @return a message indicating success or failure
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!"; // Username already taken
        }

        userRepository.save(user); // Save new user
        return "User registered successfully!"; // Registration success
    }

    /**
     * Logs in a user with the provided credentials.
     * @param username the username of the user
     * @param password the password of the user
     * @return a message indicating login success or failure
     */
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return "Login Successful!"; // User found and login success
        }
        return "Invalid Username or Password!"; // Invalid credentials
    }
}
