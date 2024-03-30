package com.cse5382.assignment.Repository;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.j256.ormlite.dao.Dao;

@Repository
public interface PhoneBookRepository extends Dao<PhoneBookEntry, String>{
    
}
