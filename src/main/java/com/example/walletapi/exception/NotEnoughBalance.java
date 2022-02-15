package com.example.walletapi.exception;

public class NotEnoughBalance extends Exception {
    public NotEnoughBalance () {
        super("User does not have enough balanace");
    }
}
