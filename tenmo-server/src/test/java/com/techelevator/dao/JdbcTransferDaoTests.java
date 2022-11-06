package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferView;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private JdbcAccountDAO sut2;

    private JdbcTransferDAO sut;

    private TransferView transferView = null;

    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal(0).setScale(2));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal(1000).setScale(2));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal(500.50).setScale(2));

    private static final User USER_1 = new User(1001, "user1", "user1", "USER");
    private static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDAO(jdbcTemplate);
        sut2 = new JdbcAccountDAO(jdbcTemplate);
        BigDecimal  bd1 = new BigDecimal(200.00).setScale(2);
        transferView = new TransferView(  2, 2,  2002, 2001,bd1);

    }

    @Test
    public void createTransferViewTest(){

        boolean transferViewCreated = sut.createTransferView(transferView);

        Assert.assertTrue(transferViewCreated);
    }

    @Test
    public void getTransfersViewByUserIdTest(){

        boolean transferViewCreated = sut.createTransferView(transferView);

        List<TransferView> expected = new ArrayList<>();
        List<TransferView> actual = sut.getTransfersViewByUserId("user2");
        expected.add(transferView);

        Assert.assertEquals(expected.size(), actual.size());
    }
    @Test
    public void UpdateFromBalancesViewTest(){
        boolean transferViewCreated = sut.createTransferView(transferView);
        boolean updatedTransfer = sut.UpdateFromBalancesView(transferView);
        Assert.assertTrue(updatedTransfer);
        BigDecimal expectedBalance = sut2.getAccountByAccountId(ACCOUNT_2.getAccountId()).getBalance().setScale(2);

        Assert.assertEquals(expectedBalance, new BigDecimal(800.00).setScale(2));

    }
    @Test
    public void UpdateToBalancesViewTest(){
        boolean transferViewCreated = sut.createTransferView(transferView);
        boolean updatedTransfer = sut.UpdateToBalancesView(transferView);
        Assert.assertTrue(updatedTransfer);
        BigDecimal expectedBalance = sut2.getAccountByAccountId(ACCOUNT_1.getAccountId()).getBalance().setScale(2);

        Assert.assertEquals(expectedBalance, new BigDecimal(200.00).setScale(2));


    }
    @Test
    public void getTransferViewByTransferIdTest(){

        boolean transferViewCreated = sut.createTransferView(transferView);

        List<TransferView> expectedList = new ArrayList<>();
        expectedList.add(transferView);

        TransferView expected = transferView;
        TransferView actual = sut.getTransferViewByTransferId(transferView.getTransferId());

        expected.setTransferId(3001);
        transferViewsMatch(transferView,sut.getTransferViewByTransferId(3001));
    }

    private void transferViewsMatch(TransferView expected, TransferView actual) {

        Assert.assertEquals(expected.getTransferId(),actual.getTransferId());
        Assert.assertEquals(expected.getTransferTypeId(),actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatusId(),actual.getTransferStatusId());
        Assert.assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(expected.getAccountTo(),actual.getAccountTo());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());

    }


}