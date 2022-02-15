package com.example.walletapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int walletId;

    private float balance;

    private boolean isActive;

    @JsonIgnore
    @OneToOne(mappedBy = "wallet")
    private User walletOfUser;

    @JsonIgnore
    @OneToMany(mappedBy = "txnToWallet")
    private List<Transactions> toWalletTxn;

    @JsonIgnore
    @OneToMany(mappedBy = "txnFromWallet")
    private List<Transactions> fromWalletTxn;

    public Wallet() {
        this.balance = 0;
        this.isActive = true;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public User getWalletOfUser() {
        return walletOfUser;
    }

    public void setWalletOfUser(User walletOfUser) {
        this.walletOfUser = walletOfUser;
    }

    public List<Transactions> getToWalletTxn() {
        return toWalletTxn;
    }

    public void setToWalletTxn(List<Transactions> toWalletTxn) {
        this.toWalletTxn = toWalletTxn;
    }

    public List<Transactions> getFromWalletTxn() {
        return fromWalletTxn;
    }

    public void setFromWalletTxn(List<Transactions> fromWalletTxn) {
        this.fromWalletTxn = fromWalletTxn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
