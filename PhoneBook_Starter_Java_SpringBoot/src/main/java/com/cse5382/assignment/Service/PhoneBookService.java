package com.cse5382.assignment.Service;

import com.cse5382.assignment.Model.PhoneBookEntry;

import java.util.List;

public interface PhoneBookService {
    public List<PhoneBookEntry> list();
    public void add(PhoneBookEntry phoneBookEntry);

    public void deleteByName(String name);

    public void deleteByNumber(String phoneNumber);
}
