package com.example.walletapi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    private String phoneNumber;
    private String fname;
    private String lname;

    @Column(unique=true)
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "walletId", referencedColumnName = "walletId")
    private Wallet wallet;



    public User() {
    }

    public User(String phoneNumber, String fname, String lname, String email, String password, Wallet wallet) {
        this.phoneNumber = phoneNumber;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
