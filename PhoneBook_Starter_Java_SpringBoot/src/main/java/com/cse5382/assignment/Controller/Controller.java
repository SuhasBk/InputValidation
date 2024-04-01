package com.cse5382.assignment.Controller;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Model.PhoneBookResponse;
import com.cse5382.assignment.Service.PhoneBookService;
import com.cse5382.assignment.Service.PhoneBookServiceImpl;
import com.cse5382.assignment.Util.AppConstants;
import com.j256.ormlite.support.ConnectionSource;

@RestController
@Validated
public class Controller {

    PhoneBookService phoneBookService;

    public Controller(ConnectionSource connectionSource, PhoneBookServiceImpl phoneBookServiceImpl) throws SQLException {
        this.phoneBookService = new PhoneBookServiceImpl(connectionSource);
    }

    @GetMapping(path = "phoneBook/list")
    public List<PhoneBookEntry> list(Authentication auth) throws Exception {
        return phoneBookService.list();
    }

    @PostMapping(path = "phoneBook/add")
    public ResponseEntity<PhoneBookResponse> add(@Valid @RequestBody PhoneBookEntry phoneBookEntry) throws Exception {
        phoneBookService.add(phoneBookEntry);
        return ResponseEntity.ok().body(new PhoneBookResponse(200, "New Entry Added"));
    }

    @PutMapping(path = "phoneBook/deleteByName")
    public ResponseEntity<PhoneBookResponse> deleteByName(
        @RequestParam 
        @Pattern(regexp = AppConstants.NAME_REGEX, message = "Please enter a valid name")
        String name) throws Exception {
        phoneBookService.deleteByName(name);
        return ResponseEntity.ok().body(new PhoneBookResponse(200, "Entry Deleted"));
    }

    @PutMapping(path = "phoneBook/deleteByNumber")
    public ResponseEntity<PhoneBookResponse> deleteByNumber(
        @RequestParam
        @Pattern(regexp = AppConstants.PHONE_NUMBER_REGEX, message = "Please enter a valid name")
        String number) throws Exception {
        phoneBookService.deleteByNumber(number);
        return ResponseEntity.ok().body(new PhoneBookResponse(200, "Entry Deleted"));
    }

}
