package com.example.walletapi.exception;

public class WalletNotFoundException extends Exception {

    public WalletNotFoundException() {
        super("User does not have wallet");
    }
}
