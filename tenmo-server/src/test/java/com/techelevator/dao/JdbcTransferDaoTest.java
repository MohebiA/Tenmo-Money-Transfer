package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferView;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private JdbcTransferDAO sut;

    private static final TransferView TRANSFER_VIEW_1 = new TransferView(  2, 2,  2002, 2001,new BigDecimal(200).setScale(2));
    private static final TransferView TRANSFER_VIEW_2 = new TransferView( 2, 2,  2003, 2002,new BigDecimal(500.50).setScale(2));
    private static final TransferView TRANSFER_VIEW_3 = new TransferView(  2, 2,  2001, 2002,new BigDecimal(1000.01).setScale(2));

    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal(0).setScale(2));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal(1000).setScale(2));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal(500.50).setScale(2));

    private static final User USER_1 = new User(1001, "user1", "user1", "USER");
    private static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");





}
