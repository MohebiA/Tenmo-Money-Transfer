package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.security.Principal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getBalance(String username) {
        long balance = 0;
        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                balance = mapRowToAccount(results).getBalance();
            }
        } catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return balance;
    }

/*    @Override
    public long getBalance(Principal principal) {
        String userId = principal.getName();
        long balance = 0;
        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = mapRowToAccount(results).getBalance();
            }
        } catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return balance;
    }*/

    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setBalance(result.getLong("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setAccountId(result.getInt("user_id"));
        return account;
    }
}
