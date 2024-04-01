package com.cse5382.assignment;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Model.PhoneBookUser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

@SpringBootApplication
public class AssignmentApplication {

	@Value("${databaseUrl}")
	String databaseUrl;

	@Value("${rUser}")
	String rUser;

	@Value("${rUserPwd}")
	String rUserPwd;

	@Value("${rwUser}")
	String rwUser;

	@Value("${rwUserPwd}")
	String rwUserPwd;

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ConnectionSource dbConfig() throws SQLException {
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseUrl);
		
		// create required tables:
		TableUtils.createTableIfNotExists(connectionSource, PhoneBookEntry.class);
		TableUtils.createTableIfNotExists(connectionSource, PhoneBookUser.class);

		// initialize tables:
		// TableUtils.clearTable(connectionSource, PhoneBookEntry.class);
		TableUtils.clearTable(connectionSource, PhoneBookUser.class);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Dao<PhoneBookUser, String> userDao = DaoManager.createDao(connectionSource, PhoneBookUser.class);
		PhoneBookUser dummyReadUser = new PhoneBookUser(rUser, encoder.encode(rUserPwd), "R");
		PhoneBookUser dummyReadWriteUser = new PhoneBookUser(rwUser, encoder.encode(rwUserPwd), "RW");

		userDao.createIfNotExists(dummyReadUser);
		userDao.createIfNotExists(dummyReadWriteUser);
		return connectionSource;
	}

}
