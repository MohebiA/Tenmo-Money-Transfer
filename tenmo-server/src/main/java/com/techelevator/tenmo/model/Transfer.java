package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Transfer {
    private int transferId;
    @NotBlank
    private int transferTypeId;
    @NotBlank
    private int transferStatusId;
    @NotBlank
    private int accountFrom;
    @NotBlank
    private int accountTo;
    @Min (value = 0)
    private long numeric;

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

    public long getNumeric() {
        return numeric;
    }

    public void setNumeric(long numeric) {
        this.numeric = numeric;
    }

    public Transfer (){}

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, long numeric) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.numeric = numeric;
    }

    public Transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, long numeric) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.numeric = numeric;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", numeric=" + numeric +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return transferId == transfer.transferId && transferTypeId == transfer.transferTypeId && transferStatusId == transfer.transferStatusId && accountFrom == transfer.accountFrom && accountTo == transfer.accountTo && numeric == transfer.numeric;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, numeric);
    }
}