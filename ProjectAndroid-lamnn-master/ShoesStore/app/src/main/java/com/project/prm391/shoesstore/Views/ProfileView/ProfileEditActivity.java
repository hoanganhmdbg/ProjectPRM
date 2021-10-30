package com.project.prm391.shoesstore.Views.ProfileView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.prm391.shoesstore.Presenter.ProfileEditPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by duytoquang on 3/19/18.
 */

public class ProfileEditActivity extends AppCompatActivity implements ProfileEditView, View.OnClickListener, View.OnFocusChangeListener {
    private final ProfileEditPresenter profileEditPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilPhoneNumber)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    private ProgressDialog progressDialog;

    public ProfileEditActivity() {
        profileEditPresenter = new ProfileEditPresenter(this);
        notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileEditPresenter.loadCurrentUserProfile();
    }

    @OnClick(R.id.btnUpdate)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnUpdate:
                profileEditPresenter.updateProfile();
        }
    }

    @OnFocusChange({R.id.etName, R.id.etEmail, R.id.etPhoneNumber, R.id.etAddress})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id) {
            case R.id.etName:
                if (!hasFocus) {
                    profileEditPresenter.validateNameAndNotifyError();
                }
                break;
            case R.id.etEmail:
                if (!hasFocus) {
                    profileEditPresenter.validateEmailAndNotifyError();
                }
                break;
            case R.id.etPhoneNumber:
                if (!hasFocus) {
                    profileEditPresenter.validatePhoneNumberAndNotifyError();
                }
                break;
            case R.id.etAddress:
                if (!hasFocus) {
                    profileEditPresenter.validateAddressAndNotifyError();
                }
                break;
        }
    }

    @Override
    public String getInputName() {
        return etName.getText().toString();
    }

    @Override
    public void setInputName(String name) {
        etName.setText(name);
    }

    @Override
    public String getInputEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void setInputEmail(String email) {
        etEmail.setText(email);
    }

    @Override
    public String getInputPhoneNumber() {
        return etPhoneNumber.getText().toString();
    }

    @Override
    public void setInputPhoneNumber(String phoneNumber) {
        etPhoneNumber.setText(phoneNumber);
    }

    @Override
    public String getInputAddress() {
        return etAddress.getText().toString();
    }

    @Override
    public void setInputAddress(String address) {
        etAddress.setText(address);
    }

    @Override
    public void notifyInputNameError(String error) {
        Gui.showInputError(tilName, error);
    }

    @Override
    public void notifyInputEmailError(String error) {
        Gui.showInputError(tilEmail, error);
    }

    @Override
    public void notifyInputPhoneNumberError(String error) {
        Gui.showInputError(tilPhoneNumber, error);
    }

    @Override
    public void notifyInputAddressError(String error) {
        Gui.showInputError(tilAddress, error);
    }

    @Override
    public void notifyUpdateProfileSuccess() {
        AlertDialog dialog = Gui.buildSimpleAlertDialog(this, getString(R.string.label_success), getString(R.string.msg_update_profile_success));
        dialog.setOnDismissListener(dialog1 -> finishActivity(true));
        dialog.show();
    }

    @Override
    public void notifyUpdateProfileFailed(String error) {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showUpdateProfileDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_update_profile), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideUpdateProfileDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishActivity(boolean success) {
        setResult(success ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        finish();
    }
}
