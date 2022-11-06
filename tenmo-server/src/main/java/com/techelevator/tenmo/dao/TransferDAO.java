package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;

import java.security.Principal;
import java.util.List;

public interface TransferDAO {

/*    boolean createTransfer(Transfer transfer);

    List<Transfer> getTransfersByUserId(Principal principal);

    List<Transfer> getTransfersByUserId(String username);

    List<Transfer> getAllTransfers();

    Transfer getTransferByTransferId(int transferId);*/

    TransferView getTransferViewByTransferId(int transferId);

/*    boolean UpdateFromBalances(Transfer transfer);

    boolean UpdateToBalances(Transfer transfer);

    boolean getTransferStatus(Transfer transfer);

    List<TransferView> getAllTransfersView();*/

    List<TransferView> getTransfersViewByUserId(String username);

    boolean createTransferView(TransferView transferView);

    boolean UpdateFromBalancesView(TransferView transferView);

    boolean UpdateToBalancesView(TransferView transferView);




}
