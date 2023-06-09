package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferView;

import java.util.List;

public interface RequestDAO {

    List<TransferView> getListPendingRequestByUserId(String username);

    int createRequestTransfer(TransferView transferView);

    boolean updateBalancesFromTransferRequest(TransferView transferView, Account fromAccount, Account toAccount, int transfer_id);



}
