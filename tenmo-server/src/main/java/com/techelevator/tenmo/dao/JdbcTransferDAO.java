package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createTransfer(Transfer transfer){
        boolean success = false;
        String sql = "INSERT INTO transfer (transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?);";
        try {
/*            jdbcTemplate.queryForObject(sql, Transfer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());*/
            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());
            success = true;
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());;
        }
        return success;
    }

    @Override
    public List<Transfer> getTransfersByUserId(String username) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer JOIN account ON account.account_id = transfer.account_to JOIN tenmo_user " +
                "ON account.user_id = tenmo_user.user_id WHERE username = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);

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

    public boolean getTransferStatus(Transfer transfer){
        boolean approved = false;
        if(transfer.getTransferStatusId() == 2){
            approved = true;
        }
        return approved;
    }


    @Override
    public boolean UpdateFromBalances(Transfer transfer) {
        boolean success = false;
        int accountNumber = transfer.getAccountFrom();

        SqlRowSet rowSet = getSqlRowSetResults(accountNumber);
        BigDecimal balance = getBalanceFromResults(rowSet);
        BigDecimal transferAmount = transfer.getTransferAmount();

        if((balance.subtract(transferAmount)).compareTo(BigDecimal.ZERO)>=0){
            String updateAccountSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
            try {
                jdbcTemplate.update(updateAccountSql, balance.subtract(transferAmount), accountNumber);
                success = true;
            }catch (ResourceAccessException ra){
                System.out.println(ra.getMessage());
            }
        }
        return success;
    }

    @Override
    public boolean UpdateToBalances(Transfer transfer) {
        boolean success = false;
        int accountNumber = transfer.getAccountTo();

        SqlRowSet rowSet = getSqlRowSetResults(accountNumber);
        BigDecimal balance = getBalanceFromResults(rowSet);
        BigDecimal transferAmount = transfer.getTransferAmount();

        String updateAccountSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(updateAccountSql, balance.add(transferAmount), accountNumber);
            success = true;
        } catch (ResourceAccessException ra){
            System.out.println(ra.getMessage());
        }

        return success;
    }

    private Transfer mapResultToTransfer(SqlRowSet result){
        Transfer transfer= new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setTransferAmount(result.getBigDecimal("amount"));
        return transfer;
    }


    private SqlRowSet getSqlRowSetResults(int accountNumber) {
        SqlRowSet rowSet = null;
        String balanceRetrieve = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(balanceRetrieve, accountNumber);
        if (results.next()) {
            rowSet = results;
        }
        return rowSet;
    }


    private BigDecimal getBalanceFromResults(SqlRowSet rowSet){
        Account account = new Account();
        return (rowSet.getBigDecimal("balance"));
    }

    public int getAccountNumber (Transfer transfer){
        int accountNumber = transfer.getAccountFrom();
        return accountNumber;
    }

    public int getUserId (int accountNumber){
        int userId = 0;

        return userId;
    }

}
