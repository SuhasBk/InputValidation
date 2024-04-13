package com.cse5382.assignment.Security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cse5382.assignment.Model.PhoneBookUser;
import com.cse5382.assignment.Service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            PhoneBookUser dbUser = userService.getUserByUsername(username);
            if (dbUser != null && encoder.matches(password, dbUser.getPassword())) {
                List<GrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority("ROLE_READ"));
                if (dbUser.getRole().equals("RW")) {
                    roles.add(new SimpleGrantedAuthority("ROLE_READ_WRITE"));
                }
                return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString(), roles);
            } else {
                throw new UsernameNotFoundException("User not found!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
