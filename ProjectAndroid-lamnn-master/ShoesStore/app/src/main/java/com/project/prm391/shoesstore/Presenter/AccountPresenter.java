package com.project.prm391.shoesstore.Presenter;

import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Views.AccountView.AccountView;


public class AccountPresenter {

    private final AccountView accountView;
    private final IUserService userService;

    public AccountPresenter(AccountView accountView) {
        this.accountView = accountView;
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void checkSignInStatus() {
        if (userService.isSignedIn()) {
            accountView.viewSignInStatus(true, userService.getCurrentUserProfile().getName());
        } else {
            accountView.viewSignInStatus(false, "");
        }
    }

    public void logout() {
        userService.signOut();
        checkSignInStatus();
    }
}
