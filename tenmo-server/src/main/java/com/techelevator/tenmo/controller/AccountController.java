package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/myAccount")

public class AccountController {

    private AccountDAO accountDAO;

    public AccountController (AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    //TODO fix username find
    @RequestMapping (path = "", method = RequestMethod.GET)
    public long getAccountBalance(@RequestParam String username){
        long balance = 0;
        try {
            balance = accountDAO.getBalance(username);
        }catch (RestClientResponseException rcr){
            System.out.println(rcr.getRawStatusCode()+" : "+rcr.getStatusText());
        }
        return balance;
    }


}
