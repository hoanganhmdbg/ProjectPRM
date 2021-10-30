package com.project.prm391.shoesstore.Views.ProfileView;

/**
 * Created by nguyen on 3/24/2018.
 */

public interface ProfileEditView {
    String getInputName();

    void setInputName(String name);

    String getInputEmail();

    void setInputEmail(String email);

    String getInputPhoneNumber();

    void setInputPhoneNumber(String phoneNumber);

    String getInputAddress();

    void setInputAddress(String address);

    void notifyInputNameError(String error);

    void notifyInputEmailError(String error);

    void notifyInputPhoneNumberError(String error);

    void notifyInputAddressError(String error);

    void notifyUpdateProfileSuccess();

    void notifyUpdateProfileFailed(String error);

    void notifyException(Exception ex);

    void showUpdateProfileDialog();

    void hideUpdateProfileDialog();

    void finishActivity(boolean success);
}
