package com.indravz.tradingapp.controller;

import com.indravz.tradingapp.model.Account;
import com.indravz.tradingapp.model.BankLink;
import com.indravz.tradingapp.model.TradeOrder;
import com.indravz.tradingapp.service.AccountService;
/*import com.indravz.tradingapp.service.BankLinkService;
import com.indravz.tradingapp.service.TradeService;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(
        origins = {"http://localhost", "https://indras.adobe-project.online"},
        allowCredentials = "true"
)
public class AccountController {

    @Autowired
    private AccountService accountService;

  /*  @Autowired
    private BankLinkService bankLinkService;

    @Autowired
    TradeService tradeService;*/

    // Get all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

    // Get accounts by userid
    @GetMapping("/user/{id}")
    public List<Account> getAccountsByUserId(@PathVariable Long id) {
        return accountService.getAccountsByUserId(id);
    }

    // Create a new account
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    // Update an account by accountid
    @PutMapping("/{id}")
    public int updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }

    // Delete an account by accountid
    @DeleteMapping("/{id}")
    public int deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);
    }

    // Endpoint to link an account with a bank
   /* @PostMapping("/{accountId}/link-bank")
    public String linkAccountToBank(@PathVariable Long accountId, @RequestBody BankLink bankLink) {
        // Set the account ID to the bank link
        bankLink.setAccountId(accountId);

        // Link the account to the bank
        int result = bankLinkService.linkAccountToBank(bankLink);
        if (result > 0) {
            return "Account successfully linked to bank.";
        } else {
            throw new IllegalStateException("Failed to link account to bank.");
        }
    }

    // Endpoint to fetch available cash for an account
    @GetMapping("/{accountId}/available-cash")
    public Double getAvailableCash(@PathVariable Long accountId) {
        // Get the available cash for the account
        Double availableCash = bankLinkService.getAvailableCash(accountId);
        if (availableCash != null) {
            return availableCash;
        } else {
            throw new IllegalArgumentException("No available cash found for account ID: " + accountId);
        }
    }

    // Endpoint to place a trade
    @PostMapping("/{accountId}/trade")
    public String placeTrade(@PathVariable Long accountId, @RequestBody TradeOrder tradeOrder) {
        // Set the account ID to the trade order
        tradeOrder.setAccountId(accountId);

        // Place the trade order
        boolean success = tradeService.placeTradeOrder(tradeOrder);
        if (success) {
            return "Trade placed successfully.";
        } else {
            throw new IllegalStateException("Failed to place trade order.");
        }
    } */
}