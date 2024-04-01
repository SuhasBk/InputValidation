package com.cse5382.assignment.Service;

import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.PhoneBookUser;

@Service
public interface UserService {
    PhoneBookUser getUserByUsername(String username) throws Exception;
    void addNewUser(PhoneBookUser user) throws Exception;
}
