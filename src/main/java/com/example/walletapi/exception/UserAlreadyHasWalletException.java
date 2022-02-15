package com.example.walletapi.exception;

public class UserAlreadyHasWalletException extends Throwable {

    public UserAlreadyHasWalletException() {
        super("User already has a wallet");
    }
}
