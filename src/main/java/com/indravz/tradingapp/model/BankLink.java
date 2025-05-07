package com.indravz.tradingapp.model;

public class BankLink {

    private Long id;
    private Long accountId;
    private String bankName;
    private String bankAccountNumber;
    private Double availableBalance;

    public BankLink() {
    }

    public BankLink(Long id, Long accountId, String bankName, String bankAccountNumber, Double availableBalance) {
        this.id = id;
        this.accountId = accountId;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.availableBalance = availableBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }
}
