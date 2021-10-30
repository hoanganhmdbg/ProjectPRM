package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Enum.AuthActionResult;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.LoginRegisterView.ResetPasswordView;

/**
 * Created by lamtu on 2018-03-06.
 */

public class ResetPasswordPresenter {
    private static final String TAG = ResetPasswordPresenter.class.getName();

    private final ResetPasswordView resetPasswordView;
    private final IUserService userService;
    private final Resources resources;

    public ResetPasswordPresenter(ResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;
        this.userService = ServiceManager.getInstance().getUserService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void resetPassword() {
        if (validateEmailAndNotifyError() == InputValidationResult.VALID) {
            String email = resetPasswordView.getInputEmail();
            resetPasswordView.showResetPasswordProgressDialog();
            userService.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        resetPasswordView.hideResetPasswordProgressDialog();
                        if (task.isSuccessful()) {
                            AuthActionResult result = task.getResult();
                            if (result == AuthActionResult.SUCCESSFUL) {
                                resetPasswordView.notifyResetPasswordSuccessful();
                            } else {
                                resetPasswordView.notifyResetPasswordFailed(resources.getString(result.getResourceId()));
                            }
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, null, e);
                            resetPasswordView.notifyException(e);
                        }
                    });
        }
    }

    public InputValidationResult validateEmailAndNotifyError() {
        String email = resetPasswordView.getInputEmail();
        InputValidationResult result = InputValidation.validateEmail(email);
        switch (result) {
            case VALID:
                resetPasswordView.notifyInputEmailError(null);
                break;
            case EMPTY_STRING:
                resetPasswordView.notifyInputEmailError(resources.getString(R.string.msg_input_email_empty));
                break;
            case INVALID_EMAIL:
                resetPasswordView.notifyInputEmailError(resources.getString(R.string.msg_input_email_invalid));
                break;
        }
        return result;
    }
}
