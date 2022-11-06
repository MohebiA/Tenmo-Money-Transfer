package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDAO sut;

    protected static final Account ACCOUNT_1 = new Account( 2001,  1001,  new BigDecimal(0).setScale(2));
    protected static final Account ACCOUNT_2 = new Account( 2002,  1002,  new BigDecimal(1000).setScale(2));
    private static final Account ACCOUNT_3 = new Account( 2003,  1003, new BigDecimal(500.50).setScale(2));

    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDAO(jdbcTemplate);
    }

    @Test
    public void getAccountByAccountIdTest(){

        Account expected = ACCOUNT_2;

        Account actual = sut.getAccountByAccountId(ACCOUNT_2.getAccountId());

        accountMatch(expected, actual);

    }

    @Test
    public void findUserIdByAccountNumberTest(){
        Assert.assertEquals(ACCOUNT_2.getUserId(), sut.findUserIdByAccountNumber(ACCOUNT_2.getAccountId()));

    }

    @Test
    public void getAccountByUserIdTest(){

        Account expected = ACCOUNT_1;

        Account actual = sut.getAccountByUserId(ACCOUNT_1.getUserId());

        accountMatch(expected, actual);
    }

    @Test
        public void getAccountByUsernameTest(){

        Account expected = ACCOUNT_3;
        Account actual = sut.getAccountByUsername(USER_3.getUsername());
        accountMatch(expected,actual);
    }


    @Test
        public void getAccountIdByUserIdTest(){
        int expectedId = ACCOUNT_2.getAccountId();
        int actualId = sut.getAccountIdByUserId(ACCOUNT_2.getUserId());
        Assert.assertEquals(expectedId,actualId);
    }

    private void accountMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(),actual.getBalance());
    }



}
