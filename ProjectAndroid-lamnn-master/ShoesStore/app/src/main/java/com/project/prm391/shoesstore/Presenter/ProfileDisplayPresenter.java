package com.project.prm391.shoesstore.Presenter;

import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Views.ProfileView.ProfileDisplayView;

/**
 * Created by nguyen on 3/24/2018.
 */

public class ProfileDisplayPresenter {
    private final ProfileDisplayView view;
    private final IUserService userService;

    public ProfileDisplayPresenter(ProfileDisplayView view) {
        this.view = view;
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void loadProfile() {
        User user = userService.getCurrentUserProfile();
        if (user != null) {
            view.displayProfile(user.getName(), user.getEmail(), user.getPhoneNumber(), user.getAddress(), user.getPhotoUrl());
        }
    }
}
