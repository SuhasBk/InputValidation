package com.cse5382.assignment;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

@SpringBootApplication
public class AssignmentApplication {

	@Value("${databaseUrl}")
	String databaseUrl;

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Bean
	ConnectionSource dbConfig() throws SQLException {
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseUrl);
		TableUtils.createTableIfNotExists(connectionSource, PhoneBookEntry.class);
		TableUtils.clearTable(connectionSource, PhoneBookEntry.class);
		return connectionSource;
	}

}
