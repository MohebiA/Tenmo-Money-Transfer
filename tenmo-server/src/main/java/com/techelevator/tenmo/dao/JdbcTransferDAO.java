package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.client.ResourceAccessException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDAO implements TransferDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTransfer(Transfer transfer){
        String sql = "INSERT INTO transfer (transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?);";
        try {
            jdbcTemplate.queryForObject(sql, Transfer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo());
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());;
        }
    }

    @Override
    public List<Transfer> getTransfersByUserId(Principal principal) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer JOIN account ON account.account_id = transfer.account_to JOIN tenmo_user " +
                "ON account.user_id = tenmo_user.user_id WHERE username = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, principal.getName());

            while (result.next()) {
                transferList.add(mapResultToTransfer(result));
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transferList;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transferList = new ArrayList<>();

        String sql = "SELECT * FROM transfer";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

            while (result.next()) {
                transferList.add(mapResultToTransfer(result));
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transferList;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        try{
            if(result.next()){
                transfer = mapResultToTransfer(result);
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    private Transfer mapResultToTransfer(SqlRowSet result){
        Transfer transfer= new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setBalance(result.getLong("balance"));
        return transfer;
    }

}
