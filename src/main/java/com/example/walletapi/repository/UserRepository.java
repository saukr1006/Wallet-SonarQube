package com.example.walletapi.repository;

import com.example.walletapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    public User findByPhoneNumber(String phoneNumber);
    public User findByEmailOrPhoneNumber(String email, String phonenumber);
}
