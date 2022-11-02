package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    public Account getBalance(String username) {
        Account account = null;
        BigDecimal balance = new BigDecimal(0);
        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccount(int accountId) {
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
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }
}
