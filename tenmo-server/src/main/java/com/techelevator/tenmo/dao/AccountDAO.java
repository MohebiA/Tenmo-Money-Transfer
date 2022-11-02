package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDAO {
    Account getBalance(String username);

    Account getAccount(int accountId);
//    long getBalance(Principal principal);

}
