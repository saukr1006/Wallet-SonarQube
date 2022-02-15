package com.example.walletapi.repository;

import com.example.walletapi.entity.Transactions;
import com.example.walletapi.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {

    public Page<Transactions> findByTxnFromWalletOrTxnToWallet(Wallet w1, Wallet w2, Pageable pageable);
    public Transactions findByTxnId(Integer txnId);
}
