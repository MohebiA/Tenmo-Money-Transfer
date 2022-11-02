package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.security.Principal;

public interface AccountDAO {
    long getBalance(String username);
//    long getBalance(Principal principal);

}
