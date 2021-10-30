package com.project.prm391.shoesstore.Views.LoginRegisterView;

import com.project.prm391.shoesstore.Enum.ExternalSignInProvider;

/**
 * Created by lamtu on 2018-03-05.
 */

public interface LoginView {
    String getInputEmail();

    String getInputPassword();

    void notifyInputEmailError(String error);

    void notifyInputPasswordError(String error);

    void notifyLoginFailed(String error);

    void notifyException(Exception ex);

    void showLoginProgressDialog();

    void hideLoginProgressDialog();

    void startGoogleSignInActivity();

    void startFacebookSignInActivity();

    void startRegisterActivity(ExternalSignInProvider provider, String email, String token);

    void startResetPasswordActivity();

    void finishActivity(boolean success);
}
