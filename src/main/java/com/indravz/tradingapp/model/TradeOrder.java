package com.indravz.tradingapp.model;

public class TradeOrder {
    private Long accountId;
    private Double amount;
    private String symbol;
    private String action;

    public TradeOrder() {
    }

    public TradeOrder(Long accountId, Double amount, String symbol, String action) {
        this.accountId = accountId;
        this.amount = amount;
        this.symbol = symbol;
        this.action = action;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Manual builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long accountId;
        private Double amount;
        private String symbol;
        private String action;

        public Builder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public TradeOrder build() {
            return new TradeOrder(accountId, amount, symbol, action);
        }
    }
}
