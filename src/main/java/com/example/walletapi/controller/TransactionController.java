package com.example.walletapi.controller;

import com.example.walletapi.WalletApiApplication;
import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.entity.Transactions;
import com.example.walletapi.exception.NotAuthorisedException;
import com.example.walletapi.exception.UserNotFoundException;
import com.example.walletapi.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransactionController {

    @Autowired
    private WalletService service;

    Logger logger = LogManager.getLogger(WalletApiApplication.class);

    @GetMapping("/transactions")
    public ResponseEntity<?> txnSummary(@RequestParam String phNo,@RequestParam Integer pageNo) {

        logger.info("Transaction Summary");
        logger.debug("User phoneNumber: " + phNo);

        try {

            Page<Transactions> txn = service.txnSummary(phNo,pageNo);
            ResponseObject res = new ResponseObject(txn.toList());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (UserNotFoundException | NotAuthorisedException e) {

            logger.debug("Exception occured: " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction-status")
    public ResponseEntity<?> txnStatus(@RequestParam Integer txnId) {

        logger.info("Transaction Status");
        logger.debug("Txn Id: " + txnId);

        Transactions txn = service.getTxnById(txnId);
        return new ResponseEntity<>(txn,HttpStatus.ACCEPTED);
    }

}
