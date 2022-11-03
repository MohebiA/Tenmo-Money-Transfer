package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/myTransfers")

public class TransferController {

    private TransferDAO transferDAO;
    private UserDao userDao;
    private AccountDAO accountDAO;
    private final String BASE_URL = "/myTransfers";

    public TransferController(TransferDAO transferDAO, UserDao userDao, AccountDAO accountDAO) {
        this.transferDAO = transferDAO;
        this.userDao = userDao;
        this.accountDAO = accountDAO;
    }

 /*   @RequestMapping (path = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(){
        List<Transfer> allTransfers = new ArrayList<>();
        try {
            allTransfers = transferDAO.getAllTransfers();
        }catch (RestClientResponseException rcr){
            System.out.println(rcr.getRawStatusCode()+" : "+rcr.getStatusText());
        }
        return allTransfers;
    }*/
    //TODO Fix return and formatting
    @RequestMapping(path = "/filter", method = RequestMethod.GET)
    public Transfer getTransferById(@RequestParam int id, Principal principal) {
        Transfer transfer = transferDAO.getTransferByTransferId(id);
        int accountNumber = transfer.getAccountFrom();
        int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
        User user = userDao.getUserById(userId);
        if (user.getUsername().equalsIgnoreCase(principal.getName())) {

            try {
                transfer = transferDAO.getTransferByTransferId(id);
            } catch (RestClientResponseException rcr) {
                System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
            }
        }
        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean createTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        int accountNumber = transfer.getAccountFrom();
        int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
        User user = userDao.getUserById(userId);
        boolean success = false;

        if (user.getUsername().equalsIgnoreCase(principal.getName())) {
            //Original Code Below here
            try {
                success = transferDAO.createTransfer(transfer);
                if (success) {
                    success = transferDAO.UpdateFromBalances(transfer);
                    if (success) {
                        success = transferDAO.UpdateToBalances(transfer);
                        return success;
                    }
                }
            } catch (RestClientResponseException rcr) {
                System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not create transfers from another account.");
        }

        return success;
    }

    //TODO fix username find
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable String username, Principal principal) {
        List<Transfer> transfers = new ArrayList<>();
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                transfers = transferDAO.getTransfersByUserId(username);
                return transfers;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not view other users' transfers.");
            }

        }
    }


}
