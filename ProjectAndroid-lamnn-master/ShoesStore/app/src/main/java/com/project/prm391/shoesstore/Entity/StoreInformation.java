package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * Created by nguyen on 3/25/2018.
 */

public class StoreInformation implements Serializable {
    private String name;
    private String email;
    private String phoneNumber;
    private String websiteUrl;
    private String address;
    private double locationLatitude;
    private double locationLongitude;
    private BankAccountInformation bankAccountInformation;

    public StoreInformation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public BankAccountInformation getBankAccountInformation() {
        return bankAccountInformation;
    }

    public void setBankAccountInformation(BankAccountInformation bankAccountInformation) {
        this.bankAccountInformation = bankAccountInformation;
    }
}
