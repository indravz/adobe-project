//package com.indravz.tradingapp.service;
//
//import com.indravz.tradingapp.model.TradeOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//
//
//@Service
//public class TradeService {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Transactional
//    public boolean placeTradeOrder(TradeOrder tradeOrder) {
//        // Get available balance for the account from bank_links table
//        String selectSql = "SELECT available_balance FROM bank_links WHERE account_id = ?";
//        Double availableCash = jdbcTemplate.queryForObject(selectSql, Double.class, tradeOrder.getAccountId());
//
//        if (availableCash == null) {
//            throw new IllegalArgumentException("Account not found or has no linked bank information.");
//        }
//
//        //  Check if there is enough balance for the trade
//        if (availableCash < tradeOrder.getAmount()) {
//            throw new IllegalStateException("Insufficient funds to place the trade.");
//        }
//
//        // Update the available balance by deducting the trade amount
//        String updateBalanceSql = "UPDATE bank_links SET available_balance = available_balance - ? WHERE account_id = ?";
//        int rowsUpdated = jdbcTemplate.update(updateBalanceSql, tradeOrder.getAmount(), tradeOrder.getAccountId());
//
//        if (rowsUpdated == 0) {
//            throw new IllegalStateException("Failed to update available balance.");
//        }
//
//        // Insert the trade record in the trades table
//        String insertTradeSql = "INSERT INTO trades (account_id, amount, symbol, action, timestamp) VALUES (?, ?, ?, ?, ?)";
//        int rowsInserted = jdbcTemplate.update(insertTradeSql,
//                tradeOrder.getAccountId(),
//                tradeOrder.getAmount(),
//                tradeOrder.getSymbol(),
//                tradeOrder.getAction(),
//                LocalDateTime.now());
//
//        if (rowsInserted == 0) {
//            throw new IllegalStateException("Failed to record trade.");
//        }
//
//        return true; // Indicate that the trade was placed successfully
//    }
//}
//
