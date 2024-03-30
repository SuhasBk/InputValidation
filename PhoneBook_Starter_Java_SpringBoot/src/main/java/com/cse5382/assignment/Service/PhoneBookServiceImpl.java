package com.cse5382.assignment.Service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cse5382.assignment.Exceptions.PhoneBookEmptyException;
import com.cse5382.assignment.Exceptions.PhoneBookEntryAlreadyExistsException;
import com.cse5382.assignment.Exceptions.PhoneBookEntryNotFoundException;
import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Repository.PhoneBookRepository;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

@Service
public class PhoneBookServiceImpl extends BaseDaoImpl<PhoneBookEntry, String> implements PhoneBookService, PhoneBookRepository {

    private static final Logger PHONE_BOOK_LOGGER = LoggerFactory.getLogger("PhoneBookLogger");
    
    public PhoneBookServiceImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, PhoneBookEntry.class);
    }

    @Override
    public List<PhoneBookEntry> list() throws PhoneBookEmptyException, SQLException {
        List<PhoneBookEntry> entries = queryForAll();
        if (entries.size() == 0) {
            throw new PhoneBookEmptyException();
        }
        PHONE_BOOK_LOGGER.info("Retrieving all entries!");
        return entries;
    }

    @Override
    public void add(PhoneBookEntry phoneBookEntry) throws PhoneBookEntryAlreadyExistsException, SQLException {
        if (idExists(phoneBookEntry.getName())) {
            throw new PhoneBookEntryAlreadyExistsException();
        }
        create(phoneBookEntry);
        PHONE_BOOK_LOGGER.info("Added new entry for - {}", phoneBookEntry.getName());
    }

    @Override
    public void deleteByName(String name) throws PhoneBookEntryNotFoundException, SQLException {
        QueryBuilder<PhoneBookEntry, String> queryBuilder = queryBuilder();
        Where<PhoneBookEntry, String> where = queryBuilder.where();
        SelectArg selectArg = new SelectArg();
        where.eq("name", selectArg);
        PreparedQuery<PhoneBookEntry> preparedQuery = queryBuilder.prepare();

        selectArg.setValue(name);
        PhoneBookEntry pbEntry = queryForFirst(preparedQuery);

        if (pbEntry != null) {
            delete(pbEntry);
            PHONE_BOOK_LOGGER.info("Deleted entry (by name) - {}: {}", pbEntry.getName(), pbEntry.getPhoneNumber());
        } else {
            throw new PhoneBookEntryNotFoundException();
        }
    }


    @Override
    public void deleteByNumber(String phoneNumber) throws PhoneBookEntryNotFoundException, SQLException {
        QueryBuilder<PhoneBookEntry, String> queryBuilder = queryBuilder();
        Where<PhoneBookEntry, String> where = queryBuilder.where();
        SelectArg selectArg = new SelectArg();
        where.eq("phoneNumber", selectArg);
        PreparedQuery<PhoneBookEntry> preparedQuery = queryBuilder.prepare();

        selectArg.setValue(phoneNumber);
        PhoneBookEntry pbEntry = queryForFirst(preparedQuery);

        if (pbEntry != null) {
            delete(pbEntry);
            PHONE_BOOK_LOGGER.info("Deleted entry (by number) - {}: {}", pbEntry.getName(), pbEntry.getPhoneNumber());
        } else {
            throw new PhoneBookEntryNotFoundException();
        }
    }
}
