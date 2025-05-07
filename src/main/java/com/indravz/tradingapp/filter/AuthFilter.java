/*
package com.indravz.tradingapp.filter;

import com.indravz.tradingapp.model.User;
import com.indravz.tradingapp.service.UserService;
import com.indravz.tradingapp.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@WebFilter("/*")
public class AuthFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    private static final List<String> EXCLUDED_PATHS = List.of("/api/users");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        if (jwt == null && httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if ("Auth".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt != null && !jwtUtil.isTokenExpired(jwt)) {
            String email = jwtUtil.extractEmail(jwt);
            httpRequest.setAttribute("authenticatedUser", email);

            // ✅ Check if user exists
            var result = userService.containsUserWithEmail(email);
            if (!result.getLeft()) {
                // 🔧 Create default user if not exists
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName("Default Name"); // You can customize this
                newUser.setPassword("default-password"); // Optionally hashed or generated

                userService.addUser(newUser);
                System.out.println("Created new user with email: " + email);
            }

        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: JWT not found or invalid.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
*/
