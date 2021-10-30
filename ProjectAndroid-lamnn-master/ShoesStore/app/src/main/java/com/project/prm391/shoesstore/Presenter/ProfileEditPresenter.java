package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Enum.AuthActionResult;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.ProfileView.ProfileEditView;

/**
 * Created by nguyen on 3/24/2018.
 */

public class ProfileEditPresenter {
    private static final String TAG = ProfileEditPresenter.class.getName();

    private final ProfileEditView view;
    private final IUserService userService;
    private final Resources resources;

    public ProfileEditPresenter(ProfileEditView view) {
        this.view = view;
        this.userService = ServiceManager.getInstance().getUserService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void loadCurrentUserProfile() {
        if (userService.isSignedIn()) {
            User profile = userService.getCurrentUserProfile();
            view.setInputName(profile.getName());
            view.setInputEmail(profile.getEmail());
            view.setInputPhoneNumber(profile.getPhoneNumber());
            view.setInputAddress(profile.getAddress());
        } else {
            view.notifyUpdateProfileFailed(resources.getString(R.string.msg_AuthActionResult_ACTION_REQUIRE_SIGN_IN));
            view.finishActivity(false);
        }
    }

    public void updateProfile() {
        if (validateNameAndNotifyError() == InputValidationResult.VALID
                && validateEmailAndNotifyError() == InputValidationResult.VALID
                && validatePhoneNumberAndNotifyError() == InputValidationResult.VALID
                && validateAddressAndNotifyError() == InputValidationResult.VALID) {
            String name = view.getInputName();
            String email = view.getInputEmail();
            String phoneNumber = view.getInputPhoneNumber();
            String address = view.getInputAddress();
            if (userService.isSignedIn()) {
                User profile = userService.getCurrentUserProfile();
                profile.setName(name);
                profile.setEmail(email);
                profile.setPhoneNumber(phoneNumber);
                profile.setAddress(address);

                view.showUpdateProfileDialog();
                userService.updateCurrentUserProfile(profile)
                        .addOnCompleteListener(task -> {
                            view.hideUpdateProfileDialog();
                            if (task.isSuccessful()) {
                                AuthActionResult result = task.getResult();
                                if (result == AuthActionResult.SUCCESSFUL) {
                                    view.notifyUpdateProfileSuccess();
                                } else {
                                    view.notifyUpdateProfileFailed(resources.getString(result.getResourceId()));
                                }
                            } else {
                                Exception e = task.getException();
                                Log.e(TAG, null, e);
                                view.notifyException(e);
                            }
                        });
            } else {
                view.notifyUpdateProfileFailed(resources.getString(R.string.msg_AuthActionResult_ACTION_REQUIRE_SIGN_IN));
            }
        }
    }

    public InputValidationResult validateNameAndNotifyError() {
        String name = view.getInputName();
        InputValidationResult result = InputValidation.validateName(name);
        switch (result) {
            case VALID:
                view.notifyInputNameError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputNameError(resources.getString(R.string.msg_input_name_empty));
                break;
        }
        return result;
    }

    public InputValidationResult validateEmailAndNotifyError() {
        String email = view.getInputEmail();
        InputValidationResult result = InputValidation.validateEmail(email);
        switch (result) {
            case VALID:
                view.notifyInputEmailError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputEmailError(resources.getString(R.string.msg_input_email_empty));
                break;
            case INVALID_EMAIL:
                view.notifyInputEmailError(resources.getString(R.string.msg_input_email_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validatePhoneNumberAndNotifyError() {
        String phoneNumber = view.getInputPhoneNumber();
        InputValidationResult result = InputValidation.validatePhoneNumber(phoneNumber);
        switch (result) {
            case VALID:
                view.notifyInputPhoneNumberError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputPhoneNumberError(resources.getString(R.string.msg_input_phone_number_empty));
                break;
            case INVALID_PHONE_NUMBER:
                view.notifyInputPhoneNumberError(resources.getString(R.string.msg_input_phone_number_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validateAddressAndNotifyError() {
        String address = view.getInputAddress();
        InputValidationResult result = InputValidation.validateAddress(address);
        switch (result) {
            case VALID:
                view.notifyInputAddressError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputAddressError(resources.getString(R.string.msg_input_address_empty));
                break;
        }
        return result;
    }
}
