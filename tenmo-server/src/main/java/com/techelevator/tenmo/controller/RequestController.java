package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.RequestDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/myRequests")
public class RequestController {

    private TransferDAO transferDAO;
    private UserDao userDao;
    private AccountDAO accountDAO;
    private RequestDAO requestDAO;
    private final String BASE_URL = "/myRequests";

    public RequestController(TransferDAO transferDAO, UserDao userDao, AccountDAO accountDAO, RequestDAO requestDAO) {
        this.transferDAO = transferDAO;
        this.userDao = userDao;
        this.accountDAO = accountDAO;
        this.requestDAO = requestDAO;
    }
    //List of Requests By User
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public List<TransferView> getListRequestsByUsername (@PathVariable String username, Principal principal) {
        List<TransferView> requests = new ArrayList<>();
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                requests = requestDAO.getListPendingRequestByUserId(username);
                return requests;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You can not view other users' requests.");
            }

        }
    }

    //Get request from client and process it
    @RequestMapping(path = "/filter", method = RequestMethod.PUT)
    public boolean processTransferRequests(@RequestBody TransferView transferView, @RequestParam int transferId, Principal principal){
        boolean success = false;
        TransferView requestedTransfer = transferDAO.getTransferViewByTransferId(transferId);
        Account fromAccount = accountDAO.getAccountByUserId(requestedTransfer.getUserId());
        Account toAccount = accountDAO.getAccountByUserId(requestedTransfer.getToUserId());

        try {
            boolean processed = requestDAO.updateBalancesFromTransferRequest(requestedTransfer, fromAccount, toAccount, transferId);
            success = processed;
        } catch (RestClientResponseException rcr){
            System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
        }
        return success;
    }

    //Create Request
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public TransferView createRequest(@Valid @RequestBody TransferView transferView, Principal principal) {
        TransferView requestTransfer = new TransferView();
        String username = principal.getName();
        Account fromAccount = accountDAO.getAccountByUsername(principal.getName());
        Account toAccount = accountDAO.getAccountByUserId(transferView.getToUserId());
        User user = userDao.getUserById(fromAccount.getUserId());
        int requestTransferId = 0;

        requestTransfer = setTransView(transferView, fromAccount);

        if(validAccount(toAccount) && !(fromAccount.equals(toAccount))) {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                try{
                    requestTransferId = requestDAO.createRequestTransfer(requestTransfer);
                } catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                } catch (Exception e) {

                }finally {
                    TransferView newTransferRequest = transferDAO.getTransferViewByTransferId(requestTransferId);
                    return newTransferRequest;
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not create transfers from another users' account.");
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a valid or another account");
        }
    }

    private TransferView setTransView (TransferView transferView, Account account) {
        TransferView updatedTransfer = new TransferView();
        updatedTransfer.setTransferTypeId(1);
        updatedTransfer.setTransferStatusId(1);
        updatedTransfer.setAccountFrom(account.getAccountId());
        updatedTransfer.setAccountTo(accountDAO.getAccountIdByUserId(transferView.getToUserId()));
        updatedTransfer.setTransferAmount(transferView.getTransferAmount());

        return updatedTransfer;
    }

    private boolean validAccount(Account lookUpAccount){
        boolean valid = false;
        Account account = lookUpAccount;
        int accountNumber = account.getAccountId();

        try {
            account = accountDAO.getAccountByAccountId(accountNumber);
            if(account != null) {
                valid = true;
            }
        } catch (Exception e){}
        finally {
            return valid;
        }
    }

    private boolean separateAccount(TransferView transferView, int fromAccount){
        boolean different = false;
        Account account = accountDAO.getAccountByUserId(transferView.getToUserId());;
        int accountNumber = account.getAccountId();
        if (!(transferView.getAccountFrom() == fromAccount)){
            different = true;
        }
        return different;
    }

}
