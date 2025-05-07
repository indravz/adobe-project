package com.indravz.tradingapp.service;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import com.indravz.tradingapp.model.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    // Fetch all users from the database
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM \"users\";"; // SQL to fetch all users

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // Mapping the result set to User object
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    // Fetch a user by ID
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                return user;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;  // Return null if no user found with the given id
        }
    }

    // Add a new user
    public int addUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword());
    }

    // Verify password
  /*public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }*/


    // Fetch a user by email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }, email);
    }


    // Add a new user and return
    public Long addUserAndReturnId(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        PreparedStatementCreator psc = con -> {
            var ps = con.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps;
        };

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        // Return the generated ID
        return keyHolder.getKey().longValue();
    }

    // Update a user's details
    public int updateUser(Long id, User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail(), id);
    }

    // Delete a user
    public int deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Pair<Boolean, Long> containsUserWithEmail(String email) {
        try {
            String sql = "SELECT id FROM users WHERE email = ?";
            Long userId = jdbcTemplate.queryForObject(sql, Long.class, email);
            return new ImmutablePair<>(true, userId);
        } catch (EmptyResultDataAccessException e) {
            return new ImmutablePair<>(false, null);
        }
    }
}
