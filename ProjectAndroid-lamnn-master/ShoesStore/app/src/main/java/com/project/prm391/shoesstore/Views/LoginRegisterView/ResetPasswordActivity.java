package com.project.prm391.shoesstore.Views.LoginRegisterView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.prm391.shoesstore.Presenter.ResetPasswordPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by lamtu on 2018-03-06.
 */

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView, View.OnClickListener, View.OnFocusChangeListener {
    private final ResetPasswordPresenter resetPasswordPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnResetPassword)
    Button btnResetPassword;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    private ProgressDialog progressDialog;

    public ResetPasswordActivity() {
        this.resetPasswordPresenter = new ResetPasswordPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);
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

    @OnClick(R.id.btnResetPassword)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnResetPassword: {
                resetPasswordPresenter.resetPassword();
                break;
            }
        }
    }

    @OnFocusChange(R.id.etEmail)
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.etEmail: {
                if (!hasFocus) {
                    resetPasswordPresenter.validateEmailAndNotifyError();
                }
                break;
            }
        }
    }

    @Override
    public String getInputEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void notifyInputEmailError(String error) {
        Gui.showInputError(tilEmail, error);
    }

    @Override
    public void notifyResetPasswordSuccessful() {
        Toast.makeText(this, getString(R.string.msg_reset_password_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyResetPasswordFailed(String error) {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showResetPasswordProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_reset_password), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideResetPasswordProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
