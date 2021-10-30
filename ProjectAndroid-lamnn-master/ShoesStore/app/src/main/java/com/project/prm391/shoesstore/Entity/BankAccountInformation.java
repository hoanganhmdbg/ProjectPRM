package com.project.prm391.shoesstore.Entity;

/**
 * Created by nguyen on 3/26/2018.
 */

public class BankAccountInformation {
    private String accountNumber;
    private String accountHolderName;
    private String bankName;

    public BankAccountInformation() {
    }

    public BankAccountInformation(String accountNumber, String accountHolderName, String bankName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
