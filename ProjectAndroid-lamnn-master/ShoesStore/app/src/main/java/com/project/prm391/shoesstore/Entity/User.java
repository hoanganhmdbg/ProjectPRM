package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * User profile.
 */
public class User implements Serializable {
    /**
     * User ID.
     * This is the UID of user in Firebase Authentication module.
     */
    private String id;
    /**
     * User email.
     * This is the email to be used in delivery information,
     * not always the same as the email used to sign in.
     */
    private String email;
    /**
     * User full name.
     */
    private String name;
    /**
     * User phone number.
     */
    private String phoneNumber;
    /**
     * User address.
     */
    private String address;
    /**
     * User photo.
     */
    private String photoUrl = "";

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
