package com.project.prm391.shoesstore.Views.ProfileView;

/**
 * Created by nguyen on 3/25/2018.
 */

public interface ChangePasswordView {
    String getInputPassword();

    String getInputConfirmPassword();

    void notifyInputPasswordError(String error);

    void notifyInputConfirmPasswordError(String error);

    void notifyChangePasswordSuccess();

    void notifyChangePasswordFailed(String error);

    void notifyException(Exception ex);

    void showChangePasswordProgressDialog();

    void hideChangePasswordProgressDialog();

    void finishActivity(boolean success);
}
