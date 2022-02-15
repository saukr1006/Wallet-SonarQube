package com.example.walletapi.exception;

public class UserDoesNotHaveWallet extends Exception {

    public UserDoesNotHaveWallet () {
        super ("User does not have a wallet associated with this number");
    }
}
