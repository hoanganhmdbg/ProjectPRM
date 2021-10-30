package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Enum.AuthActionResult;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.ProfileView.ChangePasswordView;

/**
 * Created by nguyen on 3/25/2018.
 */

public class ChangePasswordPresenter {
    private static final String TAG = ChangePasswordPresenter.class.getName();

    private final ChangePasswordView view;
    private final IUserService userService;
    private final Resources resources;

    public ChangePasswordPresenter(ChangePasswordView view) {
        this.view = view;
        this.userService = ServiceManager.getInstance().getUserService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public InputValidationResult validatePasswordAndNotifyError() {
        String password = view.getInputPassword();
        InputValidationResult result = InputValidation.validatePassword(password, Constants.MINIMUM_PASSWORD_LENGTH);
        switch (result) {
            case VALID:
                view.notifyInputPasswordError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputPasswordError(resources.getString(R.string.msg_input_password_empty));
                break;
            case PASSWORD_LENGTH_TOO_SHORT:
                view.notifyInputPasswordError(resources.getString(R.string.msg_input_password_too_short));
                break;
        }
        return result;
    }

    public InputValidationResult validateConfirmPasswordAndNotifyError() {
        String confirmPassword = view.getInputConfirmPassword();
        String password = view.getInputPassword();
        InputValidationResult result = InputValidation.validateConfirmPassword(confirmPassword, password);
        switch (result) {
            case VALID:
                view.notifyInputConfirmPasswordError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputConfirmPasswordError(resources.getString(R.string.msg_input_password_empty));
                break;
            case CONFIRM_PASSWORD_NOT_MATCH:
                view.notifyInputConfirmPasswordError(resources.getString(R.string.msg_input_confirm_password_not_match));
                break;
        }
        return result;
    }

    public void changePassword() {
        if (validatePasswordAndNotifyError() == InputValidationResult.VALID
                && validateConfirmPasswordAndNotifyError() == InputValidationResult.VALID) {
            String newPassword = view.getInputPassword();
            view.showChangePasswordProgressDialog();
            userService.changePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        view.hideChangePasswordProgressDialog();
                        if (task.isSuccessful()) {
                            AuthActionResult result = task.getResult();
                            if (result == AuthActionResult.SUCCESSFUL) {
                                view.notifyChangePasswordSuccess();
                                view.finishActivity(true);
                            } else {
                                view.notifyChangePasswordFailed(resources.getString(result.getResourceId()));
                                view.finishActivity(false);
                            }
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, null, e);
                            view.notifyException(e);
                            view.finishActivity(false);
                        }
                    });
        }
    }
}
