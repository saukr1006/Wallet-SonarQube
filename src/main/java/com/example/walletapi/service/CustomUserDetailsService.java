package com.example.walletapi.service;

import com.example.walletapi.entity.User;
import com.example.walletapi.dto.CustomUserDetails;
import com.example.walletapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public UserDetails loadUserByUsername(String phNo) throws UsernameNotFoundException {

        User user = userRepo.findByPhoneNumber(phNo);

        if(user == null)
            throw new UsernameNotFoundException("User Not found");

        return new CustomUserDetails(user);
    }

}
