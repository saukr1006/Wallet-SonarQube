package com.example.walletapi.dto;

public class TransferDetails {

    private String payerPhNo;
    private String payeePhNo;
    private float amount;

    public String getPayerPhNo() {
        return payerPhNo;
    }

    public void setPayerPhNo(String payerPhNo) {
        this.payerPhNo = payerPhNo;
    }

    public String getPayeePhNo() {
        return payeePhNo;
    }

    public void setPayeePhNo(String payeePhNo) {
        this.payeePhNo = payeePhNo;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
