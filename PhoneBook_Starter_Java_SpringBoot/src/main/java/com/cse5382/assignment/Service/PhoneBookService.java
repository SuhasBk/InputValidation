package com.cse5382.assignment.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.PhoneBookEntry;

@Service
public interface PhoneBookService {
    
    public List<PhoneBookEntry> list() throws Exception;
    
    public void add(PhoneBookEntry phoneBookEntry) throws Exception;
    
    public void deleteByName(String name) throws Exception;
    
    public void deleteByNumber(String phoneNumber) throws Exception;
}
