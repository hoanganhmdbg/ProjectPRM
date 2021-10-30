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
import com.project.prm391.shoesstore.Views.LoginRegisterView.RegisterView;

/**
 * Created by lamtu on 2018-03-05.
 */

public class RegisterPresenter {
    private static final String TAG = RegisterPresenter.class.getName();

    private final RegisterView registerView;
    private final IUserService userService;
    private final Resources resources;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        this.userService = ServiceManager.getInstance().getUserService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public InputValidationResult validateNameAndNotifyError() {
        String name = registerView.getInputName();
        InputValidationResult result = InputValidation.validateName(name);
        switch (result) {
            case VALID:
                registerView.notifyInputNameError(null);
                break;
            case EMPTY_STRING:
                registerView.notifyInputNameError(resources.getString(R.string.msg_input_name_empty));
                break;
        }
        return result;
    }

    public InputValidationResult validateEmailAndNotifyError() {
        String email = registerView.getInputEmail();
        InputValidationResult result = InputValidation.validateEmail(email);
        switch (result) {
            case VALID:
                registerView.notifyInputEmailError(null);
                break;
            case EMPTY_STRING:
                registerView.notifyInputEmailError(resources.getString(R.string.msg_input_email_empty));
                break;
            case INVALID_EMAIL:
                registerView.notifyInputEmailError(resources.getString(R.string.msg_input_email_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validatePasswordAndNotifyError() {
        String password = registerView.getInputPassword();
        InputValidationResult result = InputValidation.validatePassword(password, Constants.MINIMUM_PASSWORD_LENGTH);
        switch (result) {
            case VALID:
                registerView.notifyInputPasswordError(null);
                break;
            case EMPTY_STRING:
                registerView.notifyInputPasswordError(resources.getString(R.string.msg_input_password_empty));
                break;
            case PASSWORD_LENGTH_TOO_SHORT:
                registerView.notifyInputPasswordError(resources.getString(R.string.msg_input_password_too_short));
                break;
        }
        return result;
    }

    public InputValidationResult validateConfirmPasswordAndNotifyError() {
        String confirmPassword = registerView.getInputConfirmPassword();
        String password = registerView.getInputPassword();
        InputValidationResult result = InputValidation.validateConfirmPassword(confirmPassword, password);
        switch (result) {
            case VALID:
                registerView.notifyInputConfirmPasswordError(null);
                break;
            case EMPTY_STRING:
                registerView.notifyInputConfirmPasswordError(resources.getString(R.string.msg_input_password_empty));
                break;
            case CONFIRM_PASSWORD_NOT_MATCH:
                registerView.notifyInputConfirmPasswordError(resources.getString(R.string.msg_input_confirm_password_not_match));
                break;
        }
        return result;
    }

    public void registerUser() {
        if (validateNameAndNotifyError() == InputValidationResult.VALID
                && validateEmailAndNotifyError() == InputValidationResult.VALID
                && validatePasswordAndNotifyError() == InputValidationResult.VALID
                && validateConfirmPasswordAndNotifyError() == InputValidationResult.VALID) {
            String email = registerView.getInputEmail();
            String name = registerView.getInputName();
            String password = registerView.getInputPassword();
            ExternalSignInProvider externalSignInProvider = registerView.getExternalSignInProvider();
            String token = registerView.getToken();
            registerView.showRegisterProgressDialog();
            userService.registerUserAndSignIn(email, password, name)
                    .addOnCompleteListener(registerTask -> {
                        if (registerTask.isSuccessful()) {
                            AuthActionResult result = registerTask.getResult();
                            if (result == AuthActionResult.SUCCESSFUL) {
                                switch (externalSignInProvider) {
                                    case GOOGLE:
                                        doLinkWithGoogle(token);
                                        break;
                                    case FACEBOOK:
                                        doLinkWithFacebook(token);
                                        break;
                                    default:
                                        registerView.hideRegisterProgressDialog();
                                        registerView.finishActivity(true);
                                }
                            } else {
                                registerView.hideRegisterProgressDialog();
                                notifyRegisterError(result);
                            }
                        } else {
                            Exception e = registerTask.getException();
                            Log.e(TAG, null, e);
                            registerView.hideRegisterProgressDialog();
                            registerView.notifyException(e);
                        }
                    });
        }
    }

    private void doLinkWithGoogle(String idToken) {
        userService.linkWithGoogle(idToken)
                .addOnCompleteListener(task -> {
                    registerView.hideRegisterProgressDialog();
                    if (task.isSuccessful()) {
                        AuthActionResult result = task.getResult();
                        if (result == AuthActionResult.SUCCESSFUL) {
                            registerView.finishActivity(true);
                        } else {
                            notifyRegisterError(result);
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, null, e);
                        registerView.notifyException(e);
                    }
                });
    }

    private void doLinkWithFacebook(String accessToken) {
        userService.linkWithFacebook(accessToken)
                .addOnCompleteListener(task -> {
                    registerView.hideRegisterProgressDialog();
                    if (task.isSuccessful()) {
                        AuthActionResult result = task.getResult();
                        if (result == AuthActionResult.SUCCESSFUL) {
                            registerView.finishActivity(true);
                        } else {
                            notifyRegisterError(result);
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e(TAG, null, e);
                        registerView.notifyException(e);
                    }
                });
    }

    private void notifyRegisterError(AuthActionResult result) {
        registerView.notifyRegisterFailed(resources.getString(result.getResourceId()));
    }
}
