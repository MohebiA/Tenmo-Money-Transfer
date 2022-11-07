package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/Users")
@RestController
public class UserController {

    private AccountDAO accountDAO;
    private UserDao userDao;

    public UserController(AccountDAO accountDAO, UserDao userDao){
        this.accountDAO = accountDAO;
        this.userDao = userDao;
    }

    @RequestMapping (path = "{username}", method = RequestMethod.GET)
    public int findIdByUsername(@PathVariable String username, Principal principal) {
        User user = userDao.findByUsername(username);
        int userId = 0;
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
        } else {
                try {
                  userId = userDao.findIdByUsername(username);
                } catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                }
            }
            return userId;
        }

    @RequestMapping (path = "", method = RequestMethod.GET)
    public List<User> findAll(Principal principal) {
        List<User> users = new ArrayList<>();

                try {
                    String userName = principal.getName();
                    users = userDao.findAll();
                    users.remove(userDao.findByUsername(userName));
                } catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                }
                return users;
    }


}
