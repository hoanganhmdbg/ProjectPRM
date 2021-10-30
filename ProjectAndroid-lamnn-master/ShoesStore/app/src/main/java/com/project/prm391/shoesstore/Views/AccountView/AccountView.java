package com.project.prm391.shoesstore.Views.AccountView;

public interface AccountView {
    void viewSignInStatus(boolean isSignedIn, String name);

    void startLoginActivity();

    void startRegisterActivity();

    void startProfileViewActivity();

    void startMyOrderActivity();

    void startMyWishListActivity();

    void startStoreInformationActivity();
}
