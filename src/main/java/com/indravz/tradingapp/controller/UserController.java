package com.indravz.tradingapp.controller;

import com.indravz.tradingapp.model.User;
import com.indravz.tradingapp.service.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.indravz.tradingapp.model.User;
import com.indravz.tradingapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(
        origins = {"http://localhost", "https://indras.adobe-project.online"},
        allowCredentials = "true"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        System.out.println("Get all users" );
        return userService.getAllUsers();
    }

    // Get a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(id));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Add the user with the password hashed
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        // Set the user id from the path variable
        user.setId(id);
        int updatedRows = userService.updateUser(id, user);

        if (updatedRows > 0) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        int deletedRows = userService.deleteUser(id);

        if (deletedRows > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Successfully deleted
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkUserByEmail(@RequestParam String email) {
        Pair<Boolean, Long> result = userService.containsUserWithEmail(email);
        boolean exists = result.getLeft();
        Long userId = result.getRight();

        if (exists) {
            return ResponseEntity.ok(new java.util.HashMap<>() {{
                put("exists", true);
                put("userId", userId);
            }});
        } else {
            return ResponseEntity.ok(new java.util.HashMap<>() {{
                put("exists", false);
            }});
        }
    }
}

