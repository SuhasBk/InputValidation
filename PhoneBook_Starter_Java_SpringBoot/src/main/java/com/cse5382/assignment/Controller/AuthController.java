package com.cse5382.assignment.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cse5382.assignment.Model.AuthenticationResponse;
import com.cse5382.assignment.Model.PhoneBookUser;
import com.cse5382.assignment.Service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody PhoneBookUser loginRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

}
