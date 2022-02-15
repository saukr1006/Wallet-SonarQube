package com.example.walletapi.controller;

import com.example.walletapi.WalletApiApplication;
import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.dto.UserResponse;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.exception.*;
import com.example.walletapi.dto.TransferDetails;
import com.example.walletapi.dto.WalletResponse;
import com.example.walletapi.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

//    @Autowired
//    public KafkaTemplate<String, ResponseObject> kafkaTemplate;

//    private static String topic="WalletCreated";

    @Autowired
    private WalletService service;

    Logger logger = LogManager.getLogger(WalletApiApplication.class);

    @PostMapping("/create-wallet/{phoneNumber}")
    public ResponseEntity<ResponseObject> createWallet(@PathVariable("phoneNumber") String phNo) {

        logger.info("Wallet Creation initiated.");
        logger.debug("User phoneNumber: " + phNo);

        try {

            logger.debug("Creating Wallet");
            ResponseObject res = service.createWallet(phNo);

            logger.debug("Wallet Created");
//            kafkaTemplate.send(topic,res);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception | UserAlreadyHasWalletException e) {
            logger.debug("Exception occurred [WalletController.createWallet:47]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new WalletResponse(phNo, null, false));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping ("/add-money/{phNo}/{money}")
    public ResponseEntity<ResponseObject> addMoneyToWallet(@PathVariable String phNo,@PathVariable Float money) {

        logger.info("Adding money to wallet");
        logger.debug("User phoneNumber: " + phNo);

        try {

            logger.debug("Adding money");
            ResponseObject res = service.addMoney(phNo,money);

            logger.debug("Money successfully added");
            return new ResponseEntity<>(res,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.debug("Exception occurred [WalletController.addMoneyToWallet:67]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new WalletResponse(phNo, null, true));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("transaction")
    public ResponseEntity<ResponseObject> transferMoney(@RequestBody TransferDetails det) {  //transfers money from one wallet to another


        logger.info("Transfer Money From one wallet to another");

        try {

            ResponseObject res = service.transferMoney(det);

            logger.debug("Money successfully transferred");
            return new ResponseEntity<>(res,HttpStatus.CREATED);
        } catch (NotEnoughBalance e) {

            Wallet w = service.getWalletByPhoneNumber(det.getPayerPhNo());
            logger.debug("Exception occurred [WalletController.transferMoney:88]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new WalletResponse(det.getPayerPhNo(),Float.toString(w.getBalance()),false));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            logger.debug("Exception occured [WalletController.transferMoney:93]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed",e.getLocalizedMessage(),new WalletResponse(det.getPayerPhNo(),null,false));
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
    }
}
