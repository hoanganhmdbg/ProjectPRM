package com.project.prm391.shoesstore.Views.LoginRegisterView;

import com.project.prm391.shoesstore.Enum.ExternalSignInProvider;

/**
 * Created by lamtu on 2018-03-05.
 */

public interface RegisterView {
    ExternalSignInProvider getExternalSignInProvider();

    String getToken();

    String getInputName();

    String getInputEmail();

    String getInputPassword();

    String getInputConfirmPassword();

    void notifyInputNameError(String error);

    void notifyInputEmailError(String error);

    void notifyInputPasswordError(String error);

    void notifyInputConfirmPasswordError(String error);

    void notifyRegisterFailed(String error);

    void notifyException(Exception ex);

    void showRegisterProgressDialog();

    void hideRegisterProgressDialog();

    void finishActivity(boolean success);
}
