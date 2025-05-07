package com.indravz.tradingapp.service;
import java.time.LocalDateTime;

import com.indravz.tradingapp.model.Account;
import com.indravz.tradingapp.model.AccountType;
import com.indravz.tradingapp.model.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
   /* private final BankLinkService bankLinkService;

    public AccountService(BankLinkService bankLinkService) {
        this.bankLinkService = bankLinkService;
    }*/

    // Fetch all accounts
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Account account = new Account();
            account.setId(rs.getLong("id"));
            account.setAccountNumber(rs.getString("account_number"));
            account.setAccountType(AccountType.valueOf(rs.getString("account_type").toUpperCase()));  // Convert string to enum
            account.setUserId(rs.getLong("user_id"));
            account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            account.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return account;
        });
    }

    // Create a new account
    public Account createAccount(Account account) {
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(LocalDateTime.now());
        }
        if (account.getUpdatedAt() == null) {
            account.setUpdatedAt(LocalDateTime.now());
        }


        String sql = "INSERT INTO accounts (account_type, user_id, created_at, updated_at) VALUES (?, ?, ?, ?) RETURNING account_number";


        Long accountNumber = jdbcTemplate.queryForObject(sql,
                Long.class,
                account.getAccountType().toString(),
                account.getUserId(),
                account.getCreatedAt(),
                account.getUpdatedAt());

        account.setAccountNumber(accountNumber.toString());

        return account;
    }


// Fetch accounts by user ID
    public List<Account> getAccountsByUserId(Long userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Account account = new Account();
            account.setId(rs.getLong("id"));
            account.setAccountNumber(rs.getString("account_number"));
            account.setAccountType(AccountType.valueOf(rs.getString("account_type").toUpperCase()));  // Convert string to enum
            account.setUserId(rs.getLong("user_id"));
            account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            account.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return account;
        }, userId);
    }


    // Update an account by id
    public int updateAccount(Long id, Account account) {
        String sql = "UPDATE accounts SET account_number = ?, account_type = ?, user_id = ?, updated_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                account.getAccountNumber(),
                account.getAccountType().toString(),  // Convert enum to string
                account.getUserId(),
                account.getUpdatedAt(),
                id);
    }

    // Delete an account by id
    public int deleteAccount(Long id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
