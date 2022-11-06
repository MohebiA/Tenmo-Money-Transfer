package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
            if (result.next()){
                account = mapRowToAccount(result);
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            if (result.next()){
                account = mapRowToAccount(result);
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public int getAccountIdByUserId(int userId){
        int accountNumber;
        try {
            accountNumber = jdbcTemplate.queryForObject("SELECT account_id FROM account WHERE user_id = ?", int.class, userId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User id  " + userId + " was not found.");
        }

        return accountNumber;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Account account = null;
        String sql = "SELECT * FROM account a JOIN tenmo_user tu ON tu.user_id = a.user_id WHERE username = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            if (result.next()){
                account = mapRowToAccount(result);
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public int findUserIdByAccountNumber(int accountNumber) {
        int userId;
        try {
            userId = jdbcTemplate.queryForObject("SELECT user_id FROM account WHERE account_id = ?", int.class, accountNumber);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("Account  " + accountNumber + " was not found.");
        }

        return userId;
    }

    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();

        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));

        return account;
    }

}
