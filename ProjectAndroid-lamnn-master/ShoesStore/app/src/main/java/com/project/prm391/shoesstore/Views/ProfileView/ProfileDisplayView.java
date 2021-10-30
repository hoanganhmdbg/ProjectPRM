package com.project.prm391.shoesstore.Views.ProfileView;

/**
 * Created by nguyen on 3/24/2018.
 */

public interface ProfileDisplayView {
    void displayProfile(String name, String email, String phoneNumber, String address, String photoUrl);

    void startProfileEditActivity();

    void startChangePasswordActivity();
}
