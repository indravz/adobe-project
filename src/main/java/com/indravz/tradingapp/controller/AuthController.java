package com.indravz.tradingapp.controller;

import com.indravz.tradingapp.model.User;
import com.indravz.tradingapp.service.UserService;
import com.indravz.tradingapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        User dbUser = userService.getUserByEmail(user.getEmail());
        System.out.println("Attempting login for user: " + user.getEmail());
        //if (dbUser != null && userService.verifyPassword(user.getPassword(), dbUser.getPassword()))
        if (dbUser != null){
            String jwt = jwtUtil.generateToken(user.getEmail());

            // Add JWT as a cookie
            Cookie cookie = new Cookie("Auth", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Use true in production (HTTPS)
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 hour
            response.addCookie(cookie);
            System.out.println("successful login for user: " + user.getEmail());
            return "Login successful";
        }
        System.out.println("unsuccessful login for user: " + user.getEmail());
        return "Invalid email or password";
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        int result = userService.addUser(user);
        return result > 0 ? "User registered successfully" : "Error registering user";
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        // Return a 200 OK response for preflight OPTIONS request
        return ResponseEntity.ok().build();
    }
}
