package com.example.walletapi.exception;

public class UserAlreadyExist extends Exception{
    public UserAlreadyExist() {
        super("User already exist");
    }
}
