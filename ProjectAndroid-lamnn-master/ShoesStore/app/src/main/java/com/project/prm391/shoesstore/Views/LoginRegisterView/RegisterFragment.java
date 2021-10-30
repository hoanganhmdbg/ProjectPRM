package com.project.prm391.shoesstore.Views.LoginRegisterView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.project.prm391.shoesstore.Enum.ExternalSignInProvider;
import com.project.prm391.shoesstore.Presenter.RegisterPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Utils.Strings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by lamtu on 2018-02-25.
 */

public class RegisterFragment extends Fragment implements RegisterView, View.OnClickListener, View.OnFocusChangeListener {
    private final RegisterPresenter registerPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tilConfirmPassword)
    TextInputLayout tilConfirmPassword;
    private ProgressDialog progressDialog;
    private ExternalSignInProvider externalSignInProvider;
    private String token;

    public RegisterFragment() {
        registerPresenter = new RegisterPresenter(this);
        notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getActivity().getIntent();
        setExternalSignInProvider((ExternalSignInProvider) intent.getSerializableExtra("externalSignInProvider"));
        if (getExternalSignInProvider() != null) {
            String email = intent.getStringExtra("email");
            setToken(intent.getStringExtra("token"));
            if (Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(token)) {
                finishActivity(false);
            } else {
                lockAndSetInputEmail(email);
            }
        }
    }

    private void lockAndSetInputEmail(String email) {
        etEmail.setText(email);
        etEmail.setEnabled(false);
        etEmail.setFocusable(false);
        etEmail.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public ExternalSignInProvider getExternalSignInProvider() {
        return this.externalSignInProvider;
    }

    private void setExternalSignInProvider(ExternalSignInProvider externalSignInProvider) {
        this.externalSignInProvider = externalSignInProvider;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getInputName() {
        return etName.getText().toString();
    }

    @Override
    public String getInputEmail() {
        return etEmail.getText().toString();
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
    public void notifyInputNameError(String error) {
        Gui.showInputError(tilName, error);
    }

    @Override
    public void notifyInputEmailError(String error) {
        Gui.showInputError(tilEmail, error);
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
    public void notifyRegisterFailed(String error) {
        Gui.buildSimpleAlertDialog(getContext(), getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showRegisterProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(getContext(), getString(R.string.label_register), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideRegisterProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishActivity(boolean success) {
        getActivity().setResult(success ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @OnClick(R.id.btnRegister)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnRegister:
                registerPresenter.registerUser();
                break;
        }
    }

    @OnFocusChange({R.id.etName, R.id.etEmail, R.id.etPassword, R.id.etConfirmPassword})
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.etName: {
                if (!hasFocus) {
                    registerPresenter.validateNameAndNotifyError();
                }
                break;
            }
            case R.id.etEmail: {
                if (!hasFocus) {
                    registerPresenter.validateEmailAndNotifyError();
                }
                break;
            }
            case R.id.etPassword: {
                if (!hasFocus) {
                    registerPresenter.validatePasswordAndNotifyError();
                }
                break;
            }
            case R.id.etConfirmPassword: {
                if (!hasFocus) {
                    registerPresenter.validateConfirmPasswordAndNotifyError();
                }
                break;
            }
        }
    }
}