package com.indravz.tradingapp;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;


import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

public class RequestIdProvider {
    public String get() {
        return UUID.randomUUID().toString();  // Generates a unique request ID
    }

    @Component
    public static class JwtUtil {
        private final String SECRET_KEY = "yourSecretKey";
        private final long EXPIRATION_TIME = 3600000; // 1 hour

        public String generateToken(String email) {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }

        public boolean isTokenExpired(String token) {
            return getClaims(token).getExpiration().before(new Date());
        }

        public String extractEmail(String token) {
            return getClaims(token).getSubject();
        }

        private Claims getClaims(String token) {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }
    }
}