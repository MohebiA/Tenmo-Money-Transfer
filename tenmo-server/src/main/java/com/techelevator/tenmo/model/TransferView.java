package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

public class TransferView {
    private int transferId;
    @NotNull
    private int transferTypeId;
    @NotNull
    private int transferStatusId;
    @NotNull
    private int accountFrom;
    @NotNull
    private int accountTo;
    @Positive
    private BigDecimal transferAmount;

    private int userId;

    private String username;

    private String  toUsername;

    private int toUserId;

    private String transferTypeDesc;

    private String transferStatusDesc;

    public TransferView(){}

    public TransferView(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal transferAmount, int userId, String username, String toUsername, int toUserId) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
        this.userId = userId;
        this.username = username;
        this.toUsername = toUsername;
        this.toUserId = toUserId;
    }

    public TransferView(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal transferAmount, int userId, String username, String toUsername, int toUserId) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
        this.userId = userId;
        this.username = username;
        this.toUsername = toUsername;
        this.toUserId = toUserId;
    }

    public TransferView(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal transferAmount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    @Override
    public String toString() {
        return "TransferView{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", ac countTo=" + accountTo +
                ", transferAmount=" + transferAmount +
                ", userId=" + userId +
                ", username='" + username +
                ", toUserId=" + toUserId +
                ", toUsername=" + toUsername +
                '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferView that = (TransferView) o;
        return transferId == that.transferId && transferTypeId == that.transferTypeId && transferStatusId == that.transferStatusId && accountFrom == that.accountFrom && accountTo == that.accountTo && userId == that.userId && transferAmount.equals(that.transferAmount) && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, transferAmount, userId, username);
    }
}
