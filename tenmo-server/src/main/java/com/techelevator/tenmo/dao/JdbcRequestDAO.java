package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRequestDAO implements RequestDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcRequestDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransferView> getListPendingRequestByUserId(String username) {
       List<TransferView> requestList = new ArrayList<>();
       String sql = "SELECT * FROM vw_transfer_account_users WHERE username = ? AND transfer_status_id = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username, 1);

            while (result.next()) {
                requestList.add(mapResultToTransferView(result));
            }
        }catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return requestList;
    }


    @Override
    public int createRequestTransfer(TransferView transferView) {
        Integer newId = 0;

        String requestTransferSql = "INSERT INTO transfer (transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?) RETURNING transfer_id;";

        try{
            newId = jdbcTemplate.queryForObject(requestTransferSql, Integer.class, transferView.getTransferTypeId(), transferView.getTransferStatusId(),
                    transferView.getAccountTo(), transferView.getAccountFrom(), transferView.getTransferAmount());

        }catch(ResourceAccessException ra){
            System.out.println(ra.getMessage());
        }
        return newId;
    }

    @Override
    public boolean updateBalancesFromTransferRequest(TransferView transferView, Account fromAccount, Account toAccount, int transfer_id) {
        boolean success = false;
        BigDecimal fromBalance = fromAccount.getBalance().add(transferView.getTransferAmount());
        BigDecimal toBalance = toAccount.getBalance().subtract(transferView.getTransferAmount());

        String approvedSql = "BEGIN TRANSACTION;" +
                "UPDATE account SET balance = ? WHERE account_id = ?;" +
                "UPDATE account SET balance = ? WHERE account_id = ?;"+
                "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;" +
                "COMMIT;";

        String rejectSql = "BEGIN TRANSACTION;" +
                "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;" +
                "COMMIT;";

        try{
            if(transferView.getTransferStatusId() == 2 && (toBalance.subtract(transferView.getTransferAmount()).compareTo(BigDecimal.ZERO))>=0){
                jdbcTemplate.update(approvedSql,fromBalance, fromAccount.getAccountId(), toBalance, toAccount.getAccountId(), 2, transfer_id );
                success = true;
            }
            if(transferView.getTransferStatusId() == 3 || (toBalance.subtract(transferView.getTransferAmount()).compareTo(BigDecimal.ZERO))<0) {
                jdbcTemplate.update(rejectSql, 3, transfer_id );
                success = true;
            }

        }catch(ResourceAccessException ra){
            System.out.println(ra.getMessage());
        }

        return success;
    }


    @Override
    public boolean UpdateStatus(TransferView transferView, Account fromAccount, Account toAccount, int id) {
        return false;
    }

    private TransferView mapResultToTransferView(SqlRowSet result){
        TransferView transferView= new TransferView();
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
}
