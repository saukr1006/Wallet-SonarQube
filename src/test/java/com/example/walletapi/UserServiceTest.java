package com.example.walletapi;

import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.dto.UserResponse;
import com.example.walletapi.entity.Transactions;
import com.example.walletapi.entity.User;
import com.example.walletapi.exception.InvalidEmailException;
import com.example.walletapi.exception.NotAuthorisedException;
import com.example.walletapi.exception.UserAlreadyExist;
import com.example.walletapi.exception.UserNotFoundException;
import com.example.walletapi.repository.TransactionsRepository;
import com.example.walletapi.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private WalletService service;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private TransactionsRepository txnRepo;

    private static User user;

    @BeforeEach
    void create() throws IOException {
        String userReq = "src/test/resources/UserReq.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        user = new ObjectMapper().readValue(requestJSON, User.class);
    }

    @Test
    void testSaveUser() {
        Mockito.when(userRepo.save(user)).thenReturn(user);
        assertEquals(user,service.saveUser(user));
    }

    @Test
    void testListAllUser() throws IOException {

        String userReq = "src/test/resources/UserReqForListAllUser.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        User user2 = new ObjectMapper().readValue(requestJSON, User.class);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);

        Mockito.when(userRepo.findAll()).thenReturn(userList);
        assertEquals(userList,service.listAll());
    }


    @Test
    void testTxnById() {

        Transactions txn = new Transactions();
        txn.setTxnId(1);
        txn.setStatus("Successful");
        txn.setAmount(100);
        txn.setTimestamp(new Date());
        Mockito.when(txnRepo.findByTxnId(1)).thenReturn(txn);
        assertEquals(txn,service.getTxnById(1));
    }

    @Test
    void testExistByEmailOrPhone() {

        Mockito.when(userRepo.findByEmailOrPhoneNumber(user.getEmail(),user.getPhoneNumber())).thenReturn(user);
        assertEquals(true,service.existByEmailOrPhone(user));
    }

    @Test
    void testGetUser() throws UserNotFoundException {

        Mockito.when(userRepo.existsById(user.getPhoneNumber())).thenReturn(true);
        Mockito.when(userRepo.findByPhoneNumber(user.getPhoneNumber())).thenReturn(user);
        assertEquals(user,service.getUser(user.getPhoneNumber()));
    }

    @Test
    void testCreateUser() throws IOException, UserAlreadyExist, InvalidEmailException {

        String userRes = "src/test/resources/UserResForCreateUser.json";
        String responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));
        ResponseObject resObj = new ObjectMapper().readValue(responseJSON, ResponseObject.class);
        userRes = "src/test/resources/UserResponseObject.json";
        responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));
        UserResponse userResponse = new ObjectMapper().readValue(responseJSON, UserResponse.class);

        resObj.setObj(userResponse);

        Mockito.when(userRepo.findByEmailOrPhoneNumber(user.getEmail(),user.getPhoneNumber())).thenReturn(null);
        ResponseObject obj = service.createUser(user);
        assertEquals(true, resObj.equals(obj));
    }

    @Test
    @WithMockUser(username = "0123456789")
    void testUpdateUser() throws IOException,InvalidEmailException, UserNotFoundException, NotAuthorisedException {

        String userRes = "src/test/resources/UserResForUpdateUser.json";
        String responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));
        ResponseObject resObj = new ObjectMapper().readValue(responseJSON, ResponseObject.class);
        userRes = "src/test/resources/UserResponseObject.json";
        responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));
        UserResponse userResponse = new ObjectMapper().readValue(responseJSON, UserResponse.class);

        resObj.setObj(userResponse);

        Mockito.when(userRepo.existsById(user.getPhoneNumber())).thenReturn(true);
        Mockito.when(userRepo.findByPhoneNumber("0123456789")).thenReturn(user);
        ResponseObject obj = service.updateUser(user);
        assertEquals(resObj.getStatus(),obj.getStatus());
    }


}