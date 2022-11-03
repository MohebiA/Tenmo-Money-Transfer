package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.PrinterGraphics;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/myAccount/")

public class AccountController {

    private AccountDAO accountDAO;
    private UserDao userDao;

    public AccountController (AccountDAO accountDAO, UserDao userDao){
        this.accountDAO = accountDAO;
        this.userDao = userDao;
    }



    @RequestMapping (path = "{username}", method = RequestMethod.GET)
    public BigDecimal getAccountBalanceByUserName(@PathVariable String username, Principal principal){
        User user = userDao.findByUsername(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Account Found");
        } else {
            if(user.getUsername().equalsIgnoreCase(principal.getName())) {
                Account account;
                int userId = userDao.findIdByUsername(username);
                account = accountDAO.getAccountByUserId(userId);
                return account.getBalance();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not view other users' balances.");
            }
        }
    }

    //TODO with principal added maybe delete
/*    @RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public BigDecimal getAccount(@PathVariable int id, Principal principal){
        Account account = accountDAO.getAccountByAccountId(id);;
        if (account == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Account Found");
        } else {
            if(userDao.getUserById(id).getUsername().equalsIgnoreCase(principal.getName())) {
            account = accountDAO.getAccountByAccountId(id);
            } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Account Found");
            }
        }
        return account.getBalance();
    }*/

}
