package com.project.prm391.shoesstore.Views.LoginRegisterView;

/**
 * Created by lamtu on 2018-03-06.
 */

public interface ResetPasswordView {
    String getInputEmail();

    void notifyInputEmailError(String error);

    void notifyResetPasswordSuccessful();

    void notifyResetPasswordFailed(String error);

    void notifyException(Exception e);

    void showResetPasswordProgressDialog();

    void hideResetPasswordProgressDialog();

    void finishActivity();
}
