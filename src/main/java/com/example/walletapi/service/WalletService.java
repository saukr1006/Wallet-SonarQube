package com.example.walletapi.service;

import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.entity.Transactions;
import com.example.walletapi.entity.User;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.exception.*;
import com.example.walletapi.dto.TransferDetails;
import com.example.walletapi.dto.UserResponse;
import com.example.walletapi.dto.WalletResponse;
import com.example.walletapi.repository.TransactionsRepository;
import com.example.walletapi.repository.UserRepository;
import com.example.walletapi.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class WalletService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private TransactionsRepository transactionRepo;

    String currentUserName = null;

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

    public boolean existByEmailOrPhone(User user) {
        User existsByEmailOrPhone = userRepo.findByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber());
        if (existsByEmailOrPhone != null)
            return true;

        return false;
    }

    public void checkAuthorization() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
    }

    public Transactions getTxnById(Integer id) {
        return transactionRepo.findByTxnId(id);
    }

    public User getUser(String userPhNo) throws UserNotFoundException {
        boolean status = userRepo.existsById(userPhNo);
        if (!status) {
            throw new UserNotFoundException();
        }

        return userRepo.findByPhoneNumber(userPhNo);
    }

    public boolean checkEmail(String email) {

        Pattern pt = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pt.matcher(email);
        if (!matcher.find())
            return true;
        return false;
    }

    public ResponseObject createUser(User user) throws UserAlreadyExist,InvalidEmailException {

        if(user.getEmail() == null || user.getPassword() == null || user.getPhoneNumber() == null) {
            UserResponse obj = new UserResponse(user.getFname(), user.getLname() , false);
            ResponseObject res = new ResponseObject("Failed","Email , password or Phone Number left blank",obj);
            return res;
        }

        if(existByEmailOrPhone(user))
            throw new UserAlreadyExist();

        if(checkEmail(user.getEmail()))
            throw new InvalidEmailException("Invalid Email Address!!");

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setActive(true);

        saveUser(user);
        UserResponse obj = new UserResponse(user.getFname(), user.getLname(), true);
        ResponseObject res = new ResponseObject(obj);
        return res;
    }

    public ResponseObject updateUser(User user) throws UserNotFoundException, InvalidEmailException, NotAuthorisedException {

        User present_user = getUser(user.getPhoneNumber());

        checkAuthorization();

        if(user.getPhoneNumber().compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        if(checkEmail(user.getEmail()))
            throw new InvalidEmailException("Invalid Email Address!!");

        present_user.setFname(user.getFname());
        present_user.setLname(user.getLname());
        present_user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        present_user.setEmail(user.getEmail());
        present_user.setUpdatedAt(new Date());

        saveUser(present_user);
        UserResponse res = new UserResponse(user.getFname(), user.getLname(), true);
        return new ResponseObject("Success", "", res);
    }

    public ResponseObject deleteUser(User user) throws UserNotFoundException, InvalidEmailException, NotAuthorisedException {
        User present_user = getUser(user.getPhoneNumber());
        checkAuthorization();

        if(user.getPhoneNumber().compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        if(checkEmail(user.getEmail()))
            throw new InvalidEmailException("Invalid Email Address!!");

        Wallet w = getWalletByPhoneNumber(user.getPhoneNumber());
        user.setActive(false);
        user.setUpdatedAt(new Date());
        if(w!=null)
            w.setActive(false);

        saveUser(present_user);
        UserResponse res = new UserResponse(user.getFname(), user.getLname(), false);

        return new ResponseObject("Success", "", res);
    }

    public Wallet getWalletByPhoneNumber(String phNo) {

        User user = userRepo.findByPhoneNumber(phNo);
        return user.getWallet();
    }

    public ResponseObject createWallet(String userPhNo) throws UserNotFoundException , UserAlreadyHasWalletException, NotAuthorisedException {

        User user = getUser(userPhNo);

        checkAuthorization();

        if(user.getPhoneNumber().compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        if(user.getWallet()!=null) {
            throw new UserAlreadyHasWalletException();
        }

        user.setWallet(new Wallet());
        WalletResponse res = new WalletResponse(userPhNo, "0", true);
        return new ResponseObject("Success", "", res);
    }

    public ResponseObject addMoney(String phNo, Float money) throws UserNotFoundException, UserDoesNotHaveWallet ,NotAuthorisedException{

        User user = getUser(phNo);
        checkAuthorization();

        if(user.getPhoneNumber().compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        if(user.getWallet() == null) {
            throw new UserDoesNotHaveWallet();
        }

        Wallet w = user.getWallet();

        w.setBalance(w.getBalance()+money);
        WalletResponse res = new WalletResponse(phNo,Float.toString(w.getBalance()),true);

        return new ResponseObject("Success", "", res);
    }

    public boolean addTransaction(String status,float amount,Wallet payer,Wallet payee) {

        Transactions transactions = new Transactions(status,new Date(),amount,payer,payee);

        transactionRepo.save(transactions);

        return true;
    }

    public ResponseObject transferMoney(TransferDetails det) throws WalletNotFoundException, UserNotFoundException, NotEnoughBalance, NotAuthorisedException {

        boolean payee = userRepo.existsById(det.getPayeePhNo());
        boolean payer = userRepo.existsById(det.getPayerPhNo());

        checkAuthorization();

        if(det.getPayerPhNo().compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        if (!payee || !payer) {
            throw new UserNotFoundException();
        }

        if(getWalletByPhoneNumber(det.getPayerPhNo()) == null || getWalletByPhoneNumber(det.getPayeePhNo()) == null)
            throw new WalletNotFoundException();

        Wallet payer_wallet = getWalletByPhoneNumber(det.getPayerPhNo());
        Wallet payee_wallet = getWalletByPhoneNumber(det.getPayeePhNo());

        if(payer_wallet.getBalance() < det.getAmount()) {
            addTransaction("Failed", det.getAmount(), payer_wallet,payee_wallet);
            throw new NotEnoughBalance();
        }

        payer_wallet.setBalance(payer_wallet.getBalance() - det.getAmount());
        payee_wallet.setBalance(payee_wallet.getBalance() + det.getAmount());

        addTransaction("Successful", det.getAmount(), payer_wallet,payee_wallet);
        WalletResponse res = new WalletResponse(det.getPayerPhNo(), Float.toString(payer_wallet.getBalance()),true);

        return new ResponseObject("Success", "", res);
    }

    public Page<Transactions> txnSummary(String phNo,Integer pageNo) throws UserNotFoundException, NotAuthorisedException {

        checkAuthorization();
        if(phNo.compareTo(currentUserName) != 0)
            throw new NotAuthorisedException();

        boolean status = userRepo.existsById(phNo);
        if (!status) {
            throw new UserNotFoundException();
        }

        Wallet w = getWalletByPhoneNumber(phNo);
        Pageable pageable = PageRequest.of(pageNo,4);
        Page<Transactions> txn = transactionRepo.findByTxnFromWalletOrTxnToWallet(w,w,pageable);

        return txn;
    }

}
