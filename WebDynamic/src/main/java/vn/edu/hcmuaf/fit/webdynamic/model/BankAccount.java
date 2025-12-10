package vn.edu.hcmuaf.fit.webdynamic.model;

import java.sql.Timestamp;

public class BankAccount {
    private int id;
    private int userId;
    private String bankName;
    private String bankAccountNumber;
    private String accountName;
    private Timestamp linkedDate;
    private int status;
    private Timestamp updatedAt;

    public BankAccount(int id, int userId, String bankName, String bankAccountNumber, String accountName, Timestamp linkedDate, int status, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.accountName = accountName;
        this.linkedDate = linkedDate;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Timestamp getLinkedDate() {
        return linkedDate;
    }

    public void setLinkedDate(Timestamp linkedDate) {
        this.linkedDate = linkedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
