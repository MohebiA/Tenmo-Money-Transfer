package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createTransferView(TransferView transferView) {
        boolean success = false;

        String sql = "INSERT INTO transfer (transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?);";
        try {

            jdbcTemplate.update(sql, transferView.getTransferTypeId(), transferView.getTransferStatusId(),
                    transferView.getAccountFrom(), transferView.getAccountTo(), transferView.getTransferAmount());
            success = true;
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
            ;
        }
        return success;
    }

    @Override
    public List<TransferView> getTransfersViewByUserId(String username) {
        List<TransferView> transferList = new ArrayList<>();
        String sql = "SELECT * FROM vw_transfer_account_users WHERE username = ? AND transfer_status_id != 1;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);

            while (result.next()) {
                transferList.add(mapResultToTransferView(result));
            }
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transferList;
    }

    @Override
    public TransferView getTransferViewByTransferId(int transferId) {
        TransferView transfer = null;
        String sql = "SELECT * FROM vw_transfer_account_users WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        try {
            if (result.next()) {
                transfer = mapResultToTransferView(result);
            }
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    @Override
    public boolean UpdateFromBalancesView(TransferView transferView) {
        boolean success = false;
        int accountNumber = transferView.getAccountFrom();

        SqlRowSet rowSet = getSqlRowSetResults(accountNumber);
        BigDecimal balance = getBalanceFromResults(rowSet);
        BigDecimal transferAmount = transferView.getTransferAmount();

        if ((balance.subtract(transferAmount)).compareTo(BigDecimal.ZERO) >= 0) {
            String updateAccountSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
            try {
                jdbcTemplate.update(updateAccountSql, balance.subtract(transferAmount), accountNumber);
                success = true;
            } catch (ResourceAccessException ra) {
                System.out.println(ra.getMessage());
            }
        }
        return success;
    }

    @Override
    public boolean UpdateToBalancesView(TransferView transferView) {
        boolean success = false;
        int accountNumber = transferView.getAccountTo();

        SqlRowSet rowSet = getSqlRowSetResults(accountNumber);
        BigDecimal balance = getBalanceFromResults(rowSet);
        BigDecimal transferAmount = transferView.getTransferAmount();

        String updateAccountSql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(updateAccountSql, balance.add(transferAmount), accountNumber);
            success = true;
        } catch (ResourceAccessException ra) {
            System.out.println(ra.getMessage());
        }

        return success;
    }


    private TransferView mapResultToTransferView(SqlRowSet result) {
        TransferView transferView = new TransferView();
        transferView.setTransferId(result.getInt("transfer_id"));
        transferView.setTransferTypeId(result.getInt("transfer_type_id"));
        transferView.setTransferStatusId(result.getInt("transfer_status_id"));
        transferView.setAccountFrom(result.getInt("account_from"));
        transferView.setAccountTo(result.getInt("account_to"));
        transferView.setTransferAmount(result.getBigDecimal("amount"));
        transferView.setUserId(result.getInt("user_id"));
        transferView.setUsername(result.getString("username"));
        transferView.setToUserId(result.getInt("to_userid"));
        transferView.setToUsername(result.getString("to_username"));
        transferView.setTransferStatusDesc(result.getString("transfer_status_desc"));
        transferView.setTransferTypeDesc(result.getString("transfer_type_desc"));
        return transferView;
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


    private BigDecimal getBalanceFromResults(SqlRowSet rowSet) {
        Account account = new Account();
        return (rowSet.getBigDecimal("balance"));
    }
}
