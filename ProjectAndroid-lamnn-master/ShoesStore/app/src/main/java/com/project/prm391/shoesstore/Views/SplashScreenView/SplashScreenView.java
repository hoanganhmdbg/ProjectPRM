package com.project.prm391.shoesstore.Views.SplashScreenView;

/**
 * Created by nguyen on 3/25/2018.
 */

public interface SplashScreenView {
    void showErrorDialog(String error);

    void notifyException(Exception ex);

    void startHomeActivity(boolean firstTime);

    void showProgressBar();

    void hideProgressBar();
}
