package com.cse5382.assignment.Repository;

import org.springframework.stereotype.Repository;

import com.cse5382.assignment.Model.PhoneBookUser;
import com.j256.ormlite.dao.Dao;

@Repository
public interface UserRepository extends Dao<PhoneBookUser, String>{
    
}
