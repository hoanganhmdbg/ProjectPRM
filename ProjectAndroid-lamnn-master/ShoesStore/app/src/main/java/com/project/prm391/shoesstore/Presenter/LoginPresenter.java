package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Enum.AuthActionResult;
import com.project.prm391.shoesstore.Enum.ExternalSignInProvider;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.LoginRegisterView.LoginView;

/**
 * Created by lamtu on 2018-03-05.
 */

public class LoginPresenter {
    private static final String TAG = LoginPresenter.class.getName();

    private final LoginView loginView;
    private final IUserService userService;
    private final Resources resources;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.userService = ServiceManager.getInstance().getUserService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void loginWithEmailAndPassword() {
        if (validateEmailAndNotifyError() == InputValidationResult.VALID
                && validatePasswordAndNotifyError() == InputValidationResult.VALID) {
            String email = loginView.getInputEmail();
            String password = loginView.getInputPassword();
            loginView.showLoginProgressDialog();
            userService.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        loginView.hideLoginProgressDialog();
                        if (task.isSuccessful()) {
                            AuthActionResult result = task.getResult();
                            if (result == AuthActionResult.SUCCESSFUL) {
                                loginView.finishActivity(true);
                            } else {
                                notifyLoginError(result);
                            }
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, null, e);
                            loginView.notifyException(e);
                        }
                    });
        }
    }

    public void notifyLoginError(AuthActionResult result) {
        loginView.notifyLoginFailed(resources.getString(result.getResourceId()));
    }

    public InputValidationResult validateEmailAndNotifyError() {
        String email = loginView.getInputEmail();
        InputValidationResult result = InputValidation.validateEmail(email);
        switch (result) {
            case VALID:
                loginView.notifyInputEmailError(null);
                break;
            case EMPTY_STRING:
                loginView.notifyInputEmailError(resources.getString(R.string.msg_input_email_empty));
                break;
            case INVALID_EMAIL:
                loginView.notifyInputEmailError(resources.getString(R.string.msg_input_email_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validatePasswordAndNotifyError() {
        String password = loginView.getInputPassword();
        InputValidationResult result = InputValidation.validatePassword(password, Constants.MINIMUM_PASSWORD_LENGTH);
        switch (result) {
            case VALID:
                loginView.notifyInputPasswordError(null);
                break;
            case EMPTY_STRING:
                loginView.notifyInputPasswordError(resources.getString(R.string.msg_input_password_empty));
                break;
            case PASSWORD_LENGTH_TOO_SHORT:
                loginView.notifyInputPasswordError(resources.getString(R.string.msg_input_password_too_short));
                break;
        }
        return result;
    }

    public void loginWithGoogle(String email, String idToken) {
        userService.signInWithGoogle(email, idToken)
                .addOnCompleteListener(task -> {
                    loginView.hideLoginProgressDialog();
                    if (task.isSuccessful()) {
                        AuthActionResult result = task.getResult();
                        if (result == AuthActionResult.SUCCESSFUL) {
                            loginView.finishActivity(true);
                        } else if (result == AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST) {
                            loginView.startRegisterActivity(ExternalSignInProvider.GOOGLE, email, idToken);
                        } else {
                            notifyLoginError(result);
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, null, e);
                        loginView.notifyException(e);
                    }
                });
    }

    public void loginWithFacebook(String email, String accessToken) {
        userService.signInWithFacebook(email, accessToken)
                .addOnCompleteListener(task -> {
                    loginView.hideLoginProgressDialog();
                    if (task.isSuccessful()) {
                        AuthActionResult result = task.getResult();
                        if (result == AuthActionResult.SUCCESSFUL) {
                            loginView.finishActivity(true);
                        } else if (result == AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST) {
                            loginView.startRegisterActivity(ExternalSignInProvider.FACEBOOK, email, accessToken);
                        } else {
                            notifyLoginError(result);
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, null, e);
                        loginView.notifyException(e);
                    }
                });
    }
}
