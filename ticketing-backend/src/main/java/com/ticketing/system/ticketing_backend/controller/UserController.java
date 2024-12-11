package com.ticketing.system.ticketing_backend.controller;

import com.ticketing.system.ticketing_backend.model.User;
import com.ticketing.system.ticketing_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }

        User savedUser = userRepository.save(user);
        System.out.println("Saved User: " + savedUser);
        return "User registered successfully!";
    }


    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return "Login Successful!";
        }
        return "Invalid Username or Password!";
    }
}
