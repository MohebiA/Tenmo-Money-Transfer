package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDAO {
//    Account getBalance(String username);

    Account getAccountByAccountId(int accountId);

    Account getAccountByUserId(int userId);
//    long getBalance(Principal principal);

    int findUserIdByAccountNumber(int accountNumber);

    Account getAccountByUsername(String username);

    int getAccountIdByUserId(int userId);


}
