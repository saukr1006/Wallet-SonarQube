package com.example.walletapi.dto;

public class UserResponse {

    private String fname;
    private String lname;
    private boolean isActive;

    public UserResponse() {
    }

    public UserResponse(String fname, String lname, boolean isActive) {
        this.fname = fname;
        this.lname = lname;
        this.isActive = isActive;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object newUserRes) {
        UserResponse userRes = (UserResponse) newUserRes;
        return fname.compareTo(userRes.getFname()) == 0 && lname.compareTo(userRes.getLname()) == 0 && isActive == userRes.isActive();
    }
}
