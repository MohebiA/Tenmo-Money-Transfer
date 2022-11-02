package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDAO {

    boolean createTransfer(Transfer transfer);

//    List<Transfer> getTransfersByUserId(Principal principal);

    List<Transfer> getTransfersByUserId(String username);

    List<Transfer> getAllTransfers();

    Transfer getTransferByTransferId(int transferId);

    boolean UpdateFromBalances(Transfer transfer);

    boolean UpdateToBalances(Transfer transfer);

    boolean getTransferStatus(Transfer transfer);


}
