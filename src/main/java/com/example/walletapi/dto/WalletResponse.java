package com.example.walletapi.dto;

public class WalletResponse {
    private String phNo;
    private String balance;
    private boolean isActive;

    public WalletResponse() {
    }

    public WalletResponse(String phNo, String balance, boolean isActive) {
        this.phNo = phNo;
        this.balance = balance;
        this.isActive = isActive;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

