//package com.example.walletapi.kafka;
//
//import com.example.walletapi.dto.ResponseObject;
//import com.example.walletapi.dto.WalletResponse;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class kafkaConsumer {
//    ResponseObject res = new ResponseObject();
//
//    @KafkaListener(topics = "WalletCreated",groupId = "group_json",containerFactory = "userWalletCreated")
//    public ResponseObject getTransactionsFromKafka(ResponseObject walletResponse) {
//        ResponseObject walletResponse1 = new ResponseObject(walletResponse.getStatus(), walletResponse.getError(), new WalletResponse(((WalletResponse) walletResponse.getObj()).getPhNo(), ((WalletResponse) walletResponse.getObj()).getBalance(), ((WalletResponse) walletResponse.getObj()).isActive()));
//        res = walletResponse1;
//        return res;
//    }
//}
