package com.example.walletapi.exception;

public class NotAuthorisedException extends Exception {

    public NotAuthorisedException () {
        super("You are not authorised");
    }
}
