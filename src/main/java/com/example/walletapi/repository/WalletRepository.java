package com.example.walletapi.repository;

import com.example.walletapi.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {

}
