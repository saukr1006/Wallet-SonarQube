package com.example.walletapi.controller;

import com.example.walletapi.dto.JwtReq;
import com.example.walletapi.dto.jwtRes;
import com.example.walletapi.service.CustomUserDetailsService;
import com.example.walletapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtReq authReq) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new Exception("Incorrect Username or Password",e);
        }
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authReq.getUsername());

        final  String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new jwtRes(jwt));
    }
}
