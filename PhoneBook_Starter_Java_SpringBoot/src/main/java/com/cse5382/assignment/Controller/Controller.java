package com.cse5382.assignment.Controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Service.PhoneBookService;
import com.cse5382.assignment.Service.PhoneBookServiceImpl;
import com.j256.ormlite.support.ConnectionSource;

@RestController
public class Controller {

    PhoneBookService phoneBookService;

    public Controller(ConnectionSource connectionSource, PhoneBookServiceImpl phoneBookServiceImpl) throws SQLException {
        this.phoneBookService = new PhoneBookServiceImpl(connectionSource);
    }

    @GetMapping(path = "phoneBook/list")
    public List<PhoneBookEntry> list() throws Exception {
        return phoneBookService.list();
    }

    @PostMapping(path = "phoneBook/add")
    public ResponseEntity<?> add(@RequestBody PhoneBookEntry phoneBookEntry) throws Exception {
        phoneBookService.add(phoneBookEntry);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "phoneBook/deleteByName")
    public ResponseEntity<?> deleteByName(@RequestParam String name) throws Exception {
        phoneBookService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "phoneBook/deleteByNumber")
    public ResponseEntity<?> deleteByNumber(@RequestParam String number) throws Exception {
        phoneBookService.deleteByNumber(number);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
