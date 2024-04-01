package com.cse5382.assignment.Service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.PhoneBookUser;
import com.cse5382.assignment.Repository.UserRepository;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

@Service
public class UserServiceImpl extends BaseDaoImpl<PhoneBookUser, String> implements UserService, UserRepository {

    public UserServiceImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, PhoneBookUser.class);
    }

    @Override
    public PhoneBookUser getUserByUsername(String username) throws SQLException {
        return queryForId(username);
    }

    @Override
    public void addNewUser(PhoneBookUser user) throws SQLException {
        create(user);
    }
    
}
