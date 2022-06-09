package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public BigDecimal getBalance(int userId) {

        BigDecimal balance = null;
        String sql = "SELECT balance FROM account " +
                "JOIN tenmo_user USING(user_id)" +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        return null;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        return null;
    }

    @Override
    public void updateAccount(Account account) {

    }
}
