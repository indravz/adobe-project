/*
package com.indravz.tradingapp.service;

import com.indravz.tradingapp.model.BankLink;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BankLinkService {

    private final JdbcTemplate jdbcTemplate;

    public BankLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to link an account with a bank
    public int linkAccountToBank(BankLink bankLink) {
        String sql = "INSERT INTO bank_links (account_id, bank_name, bank_account_number, available_balance) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                bankLink.getAccountId(),
                bankLink.getBankName(),
                bankLink.getBankAccountNumber(),
                bankLink.getAvailableBalance());
    }

    // Method to get available cash for a given account
    public Double getAvailableCash(Long accountId) {
        String sql = "SELECT available_balance FROM bank_links WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, accountId);
    }
}
*/
