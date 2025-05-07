package com.indravz.tradingapp.model;

public enum AccountType {
    INDIVIDUAL("individual"),
    JOINT("joint");

    private final String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
