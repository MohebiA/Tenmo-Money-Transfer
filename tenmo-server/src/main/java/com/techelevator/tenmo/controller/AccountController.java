package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.awt.print.PrinterGraphics;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/myAccount")

public class AccountController {

    private AccountDAO accountDAO;

    public AccountController (AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    //TODO fix username find
/*    @RequestMapping (path = "/{username}", method = RequestMethod.GET)
    public Account getAccountBalance(@PathVariable String username){
        Account account = null;
        BigDecimal balance = new BigDecimal(0);
        try {
            account = accountDAO.getBalance(username);
        }catch (RestClientResponseException rcr){
            System.out.println(rcr.getRawStatusCode()+" : "+rcr.getStatusText());
        }
        return account;
    }*/

   @RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public BigDecimal getAccount(@PathVariable int id){
        Account account = null;
        account = accountDAO.getAccount(id);

        return account.getBalance();
    }

}
