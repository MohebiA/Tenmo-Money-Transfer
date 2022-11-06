package com.techelevator.tenmo.controller;

import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;
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

    //TODO have to switch out the user id to username.  Readjust the transfer model to include username and adjust SQL to join tenmo so we can get username
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

/*    @RequestMapping(path = "/filter", method = RequestMethod.GET)
    public Transfer getTransferById(@RequestParam int id, Principal principal) {
        Transfer transfer = null;
        User user = null;
        try {
            int accountNumber = transferDAO.getTransferByTransferId(id).getAccountFrom();
            int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
            user = userDao.getUserById(userId);
        }
        catch (Exception e){}

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfer Found");
        } else {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                try {
                    transfer = transferDAO.getTransferByTransferId(id);
                }
                catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                }
                finally {
                    return transfer;
                }
        } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not view other users' transfers.");
            }
        }

    }*/

    //Only your transaction details
/*    @RequestMapping(path = "/filter", method = RequestMethod.GET)
    public TransferView getTransferById(@RequestParam int id, Principal principal) {
        TransferView transfer = null;
        User user = null;
        try {
            int accountNumber = transferDAO.getTransferViewByTransferId(id).getAccountFrom();
            int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
            user = userDao.getUserById(userId);
        }
        catch (Exception e){}

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfer Found");
        } else {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                try {
                    transfer = transferDAO.getTransferViewByTransferId(id);
                }
                catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                }
                finally {
                    return transfer;
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not view other users' transfers.");
            }
        }

    }*/

    //Everyone transaction details
    @RequestMapping(path = "/filter", method = RequestMethod.GET)
    public TransferView getTransferById(@RequestParam int id, Principal principal) {
        TransferView transfer = null;
        User user = null;
        try {
            int accountNumber = transferDAO.getTransferViewByTransferId(id).getAccountFrom();
            int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
            user = userDao.getUserById(userId);
        }
        catch (Exception e){}

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfer Found");
        } else {
                try {
                    transfer = transferDAO.getTransferViewByTransferId(id);
                }
                catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                }
                finally {
                    return transfer;
                }
            }
        }



/*    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean createTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        int accountNumber = transfer.getAccountFrom();
        int userId = accountDAO.findUserIdByAccountNumber(accountNumber);
        User user = userDao.getUserById(userId);
        boolean success = false;

        if(validAccount(transfer.getAccountTo()) && separateAccount(transfer)) {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                //Original Code Below here

                //TODO combine these into one method and roll the SQL commands in a transaction?
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
                } catch (Exception e) {
                } finally {
                    if (!success) {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insufficient Funds");
                    } else {
                        return success;
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not create transfers from another users' account.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a valid or another account");
        }
    }*/

/*    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean createTransfer(@Valid @RequestBody TransferView transferView, Principal principal) {
        String username = principal.getName();
        Account account = accountDAO.getAccountByUsername(principal.getName());
        TransferView addTransfer = new TransferView();

        addTransfer.setTransferTypeId(2);
        addTransfer.setTransferStatusId(2);
        addTransfer.setAccountFrom(account.getAccountId());
        addTransfer.setAccountTo(accountDAO.getAccountIdByUserId(transferView.getToUserId()));
        addTransfer.setTransferAmount(transferView.getTransferAmount());



        User user = userDao.getUserById(account.getUserId());
        boolean success = false;

        if(validAccount(account) && separateAccount(transferView, account.getAccountId())) {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                //Original Code Below here

                //TODO combine these into one method and roll the SQL commands in a transaction?
                try {
                    success = transferDAO.createTransferView(addTransfer);
                    if (success) {
                        success = transferDAO.UpdateFromBalancesView(addTransfer);
                        if (success) {
                            success = transferDAO.UpdateToBalancesView(addTransfer);
                            return success;
                        }
                    }
                } catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                } catch (Exception e) {
                } finally {
                    if (!success) {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insufficient Funds");
                    } else {
                        return success;
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not create transfers from another users' account.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a valid or another account");
        }
    }*/
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public TransferView createTransfer(@Valid @RequestBody TransferView transferView, Principal principal) {
        String username = principal.getName();
        Account account = accountDAO.getAccountByUsername(principal.getName());
        TransferView addTransfer = new TransferView();

        addTransfer.setTransferTypeId(2);
        addTransfer.setTransferStatusId(2);
        addTransfer.setAccountFrom(account.getAccountId());
        addTransfer.setAccountTo(accountDAO.getAccountIdByUserId(transferView.getToUserId()));
        addTransfer.setTransferAmount(transferView.getTransferAmount());

        User user = userDao.getUserById(account.getUserId());
        boolean success = false;

        if(validAccount(account) && separateAccount(transferView, account.getAccountId())) {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                //Original Code Below here

                //TODO combine these into one method and roll the SQL commands in a transaction?
                try {
                    success = transferDAO.createTransferView(addTransfer);
                    if (success) {
                        success = transferDAO.UpdateFromBalancesView(addTransfer);
                        if (success) {
                            success = transferDAO.UpdateToBalancesView(addTransfer);
                            return addTransfer;
                        }
                    }
                } catch (RestClientResponseException rcr) {
                    System.out.println(rcr.getRawStatusCode() + " : " + rcr.getStatusText());
                } catch (Exception e) {
                } finally {
                    if (!success) {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insufficient Funds");
                    } else {
                        return addTransfer;
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can not create transfers from another users' account.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Please enter a valid or another account");
        }
    }

/*    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
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
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You can not view other users' transfers.");
            }

        }
    }*/

    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public List<TransferView> getTransfersViewByUserId(@PathVariable String username, Principal principal) {
        List<TransferView> transfers = new ArrayList<>();
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            if (user.getUsername().equalsIgnoreCase(principal.getName())) {
                transfers = transferDAO.getTransfersViewByUserId(username);
                return transfers;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You can not view other users' transfers.");
            }

        }
    }

    //Helper Methods
/*    private boolean validAccount(int accountNumber){
        boolean valid = false;
        Account account = null;
        try {
            account = accountDAO.getAccountByAccountId(accountNumber);
            if(account != null) {
                valid = true;
            }
        } catch (Exception e){};

        return valid;

    }*/

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
