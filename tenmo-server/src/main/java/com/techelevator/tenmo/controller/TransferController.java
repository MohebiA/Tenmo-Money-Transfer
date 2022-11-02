package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/myTransfers")

public class TransferController {

    private TransferDAO transferDAO;
    private final String BASE_URL = "/myTransfers";

    public TransferController (TransferDAO transferDAO){
        this.transferDAO = transferDAO;
    }

    @RequestMapping (path = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(){
        List<Transfer> allTransfers = new ArrayList<>();
        try {
            allTransfers = transferDAO.getAllTransfers();
        }catch (RestClientResponseException rcr){
            System.out.println(rcr.getRawStatusCode()+" : "+rcr.getStatusText());
        }
        return allTransfers;
    }

    @RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        Transfer transfer = null;
        try{
            transfer = transferDAO.getTransferByTransferId(id);
        } catch (RestClientResponseException rcr) {
            System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
        }
        return transfer;
    }

    @ResponseStatus (HttpStatus.CREATED)
    @RequestMapping (path = "", method = RequestMethod.POST)
    public boolean createTransfer(@Valid @RequestBody Transfer transfer){
        boolean success = false;
        try {
            transferDAO.createTransfer(transfer);
            success = true;
/*            transferDAO.UpdateFromBalances(transfer);
            transferDAO.UpdateToBalances(transfer);*/
        } catch (RestClientResponseException rcr) {
            System.out.println(rcr.getRawStatusCode()+" : "+rcr.getStatusText());
        }
        return success;

    }
    //TODO fix username find
    @RequestMapping (path = "/username", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@RequestParam String username) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            transferDAO.getTransfersByUserId(username);
        }catch (RestClientResponseException rcr) {
            System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
        }
        return transfers;
    }

}
