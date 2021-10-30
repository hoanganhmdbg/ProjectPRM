package com.project.prm391.shoesstore.Views.ProfileView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.prm391.shoesstore.Presenter.ChangePasswordPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class ChangePasswordActivity extends AppCompatActivity implements ChangePasswordView, View.OnClickListener, View.OnFocusChangeListener {
    private final ChangePasswordPresenter changePasswordPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tilConfirmPassword)
    TextInputLayout tilConfirmPassword;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;
    private ProgressDialog progressDialog;

    public ChangePasswordActivity() {
        this.changePasswordPresenter = new ChangePasswordPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
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

    @OnClick(R.id.btnChangePassword)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangePassword:
                changePasswordPresenter.changePassword();
                break;
        }
    }

    @OnFocusChange({R.id.etPassword, R.id.etConfirmPassword})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etPassword:
                if (!hasFocus) {
                    changePasswordPresenter.validatePasswordAndNotifyError();
                }
                break;
            case R.id.etConfirmPassword:
                if (!hasFocus) {
                    changePasswordPresenter.validateConfirmPasswordAndNotifyError();
                }
                break;
        }
    }

    @Override
    public String getInputPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public String getInputConfirmPassword() {
        return etConfirmPassword.getText().toString();
    }

    @Override
    public void notifyInputPasswordError(String error) {
        Gui.showInputError(tilPassword, error);
    }

    @Override
    public void notifyInputConfirmPasswordError(String error) {
        Gui.showInputError(tilConfirmPassword, error);
    }

    @Override
    public void notifyChangePasswordSuccess() {
        notificationService.displayToast(getString(R.string.msg_change_password_success));
    }

    @Override
    public void notifyChangePasswordFailed(String error) {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showChangePasswordProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_change_password), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideChangePasswordProgressDialog() {
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
