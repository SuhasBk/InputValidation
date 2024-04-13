package com.cse5382.assignment.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cse5382.assignment.Filter.JwtService;
import com.cse5382.assignment.Model.AuthenticationResponse;
import com.cse5382.assignment.Model.PhoneBookUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(PhoneBookUser loginRequest) throws Exception {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        PhoneBookUser user = userService.getUserByUsername(loginRequest.getUsername());

        return AuthenticationResponse.builder()
            .token(jwtService.generatetoken(user))
            .build();
    }
}
