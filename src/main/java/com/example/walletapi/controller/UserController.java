package com.example.walletapi.controller;

import com.example.walletapi.WalletApiApplication;
import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.entity.User;
import com.example.walletapi.dto.UserResponse;
import com.example.walletapi.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private WalletService service;

    Logger logger = LogManager.getLogger(WalletApiApplication.class);

    @GetMapping("/users")
    public List<User> show() {
        logger.info("Get All User");
        return service.listAll();
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseObject> createUser(@RequestBody User user) {

        logger.info("User Creation initiated.");
        logger.debug("User phoneNumber: " + user.getPhoneNumber());

        try {

            logger.debug("Checking validations on user");
            ResponseObject res = service.createUser(user);
            logger.debug(res);
            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.debug("Exception occurred [UserController.createUser:47]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new UserResponse(user.getFname(), user.getLname(), false));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody User user) {
        logger.info("User Updation initiated.");
        logger.debug("User phoneNumber: " + user.getPhoneNumber());

        try {

            ResponseObject res = service.updateUser(user);

            logger.debug("User Updated");

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.debug("Exception occurred [UserController.updateUser:68]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new UserResponse(user.getFname(), user.getLname(), false));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseObject> deleteUser(@RequestBody User user) {

        logger.info("User Deletion initiated.");

        try {

            ResponseObject res = service.deleteUser(user);
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.debug("Exception occurred [UserController.deleteUser:84]: " + e.getLocalizedMessage());
            ResponseObject res = new ResponseObject("Failed", e.getLocalizedMessage(), new UserResponse(user.getFname(), user.getLname(), false));
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logoutSuccessful")
    public ResponseEntity<?> postLogout() {
        return ResponseEntity.ok().body("Logout successful.");
    }
}
