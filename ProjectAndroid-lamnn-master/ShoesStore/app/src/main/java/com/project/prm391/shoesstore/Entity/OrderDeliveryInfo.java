package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

public class OrderDeliveryInfo implements Serializable {
    private String name = "";
    private String email = "";
    private String phoneNumber = "";
    private String address = "";

    public OrderDeliveryInfo() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
