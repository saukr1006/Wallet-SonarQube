package com.example.walletapi;

import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.dto.UserResponse;
import com.example.walletapi.dto.WalletResponse;
import com.example.walletapi.entity.User;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.exception.NotAuthorisedException;
import com.example.walletapi.exception.UserAlreadyHasWalletException;
import com.example.walletapi.exception.UserDoesNotHaveWallet;
import com.example.walletapi.exception.UserNotFoundException;
import com.example.walletapi.repository.TransactionsRepository;
import com.example.walletapi.repository.UserRepository;
import com.example.walletapi.repository.WalletRepository;
import com.example.walletapi.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WalletServiceTest {

    @Autowired
    private WalletService service;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private WalletRepository walletRepo;

    private static User user;

    @BeforeEach
    void create() throws IOException {
        String userReq = "src/test/resources/UserReq.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        user = new ObjectMapper().readValue(requestJSON, User.class);
    }

    @Test
    @WithMockUser(username = "0123456789")
    void testCreateWallet() throws IOException, UserNotFoundException, UserAlreadyHasWalletException, NotAuthorisedException {
        String walletRes = "src/test/resources/WalletDTO/WalletResForCreateWallet.json";
        String responseJSON = new String(Files.readAllBytes(Paths.get(walletRes)));
        ResponseObject resObj = new ObjectMapper().readValue(responseJSON, ResponseObject.class);
        walletRes = "src/test/resources/WalletDTO/WalletResponseObjectForCreateWallet.json";
        responseJSON = new String(Files.readAllBytes(Paths.get(walletRes)));
        WalletResponse walletResponse = new ObjectMapper().readValue(responseJSON, WalletResponse.class);

        resObj.setObj(walletResponse);

        Mockito.when(userRepo.existsById(user.getPhoneNumber())).thenReturn(true);
        Mockito.when(userRepo.findByPhoneNumber("0123456789")).thenReturn(user);

        ResponseObject obj = service.createWallet("0123456789");

        System.out.println(resObj.equals(obj));
        assertEquals(resObj.getStatus(),obj.getStatus());
    }



}
