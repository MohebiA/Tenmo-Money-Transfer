package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDAO {

    void createTransfer(Transfer transfer);

    List<Transfer> getTransfersByUserId(Principal principal);

    List<Transfer> getAllTransfers();

    Transfer getTransferByTransferId(int transferId);


}
