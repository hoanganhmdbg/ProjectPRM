package com.project.prm391.shoesstore.Views.LoginRegisterView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Configs.RequestCode;
import com.project.prm391.shoesstore.Enum.ExternalSignInProvider;
import com.project.prm391.shoesstore.Presenter.LoginPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by lamtu on 2018-02-25.
 */

public class LoginFragment extends Fragment implements LoginView, View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = LoginFragment.class.getName();
    private final LoginPresenter loginPresenter;
    private final INotificationService notificationService;

    @BindView(R.id.btnLoginFacebook)
    Button btnLoginFacebook;
    @BindView(R.id.btnLoginGoogle)
    Button btnLoginGoogle;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.tvResetPassword)
    TextView tvResetPassword;
    private ProgressDialog progressDialog;
    private LoginManager facebookLoginManager;
    private CallbackManager facebookCallbackManager;

    public LoginFragment() {
        loginPresenter = new LoginPresenter(this);
        notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        setupFacebook();
        return view;
    }

    private void setupFacebook() {
        this.facebookLoginManager = LoginManager.getInstance();
        this.facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginManager.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult.getRecentlyGrantedPermissions().containsAll(Constants.FACEBOOK_PERMISSIONS)) {
                    requestEmailThenLoginWithFacebook(loginResult.getAccessToken());
                } else {
                    facebookLoginManager.logOut();
                    notifyLoginFailed(getString(R.string.msg_facebook_permission_error));
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException ex) {
                notifyException(ex);
            }
        });
    }

    private void requestEmailThenLoginWithFacebook(AccessToken accessToken) {
        showLoginProgressDialog();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
            FacebookRequestError error = response.getError();
            if (error == null) {
                String email = object.optString("email");
                loginPresenter.loginWithFacebook(email, accessToken.getToken());
            } else {
                hideLoginProgressDialog();
                Exception e = error.getException();
                if (e != null) {
                    Log.e(TAG, null, e);
                    notifyException(e);
                } else {
                    notifyLoginFailed(error.getErrorMessage());
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "email");
        request.setParameters(params);
        request.executeAsync();
    }

    @OnClick({R.id.btnLogin, R.id.btnLoginFacebook, R.id.btnLoginGoogle, R.id.tvResetPassword})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                loginPresenter.loginWithEmailAndPassword();
                break;
            }
            case R.id.btnLoginFacebook: {
                startFacebookSignInActivity();
                break;
            }
            case R.id.btnLoginGoogle: {
                startGoogleSignInActivity();
                break;
            }
            case R.id.tvResetPassword: {
                startResetPasswordActivity();
                break;
            }
        }
    }

    @OnFocusChange({R.id.etEmail, R.id.etPassword})
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.etEmail:
                if (!hasFocus) {
                    loginPresenter.validateEmailAndNotifyError();
                }
                break;
            case R.id.etPassword:
                if (!hasFocus) {
                    loginPresenter.validatePasswordAndNotifyError();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.LOGIN_GOOGLE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    showLoginProgressDialog();
                    loginPresenter.loginWithGoogle(account.getEmail(), account.getIdToken());
                } catch (ApiException e) {
                    Log.e(TAG, null, e);
                }
                break;
            case RequestCode.REGISTER:
                if (resultCode == Activity.RESULT_OK) {
                    finishActivity(true);
                }
                break;
        }
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
    public void notifyInputEmailError(String error) {
        Gui.showInputError(tilEmail, error);
    }

    @Override
    public void notifyInputPasswordError(String error) {
        Gui.showInputError(tilPassword, error);
    }

    @Override
    public void notifyLoginFailed(String error) {
        Gui.buildSimpleAlertDialog(getContext(), getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showLoginProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(getContext(), getString(R.string.label_login), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideLoginProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startGoogleSignInActivity() {
        GoogleSignInClient client = ServiceManager.getInstance().getGoogleSignInClient();
        client.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent signInIntent = client.getSignInIntent();
                startActivityForResult(signInIntent, RequestCode.LOGIN_GOOGLE);
            } else {
                notificationService.displayToastForException(task.getException());
            }
        });
    }

    @Override
    public void startFacebookSignInActivity() {
        facebookLoginManager.logOut();
        facebookLoginManager.logInWithReadPermissions(this, Constants.FACEBOOK_PERMISSIONS);
    }

    @Override
    public void startRegisterActivity(ExternalSignInProvider externalSignInProvider, String email, String token) {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.putExtra("externalSignInProvider", externalSignInProvider);
        intent.putExtra("email", email);
        intent.putExtra("token", token);
        startActivityForResult(intent, RequestCode.REGISTER);
    }

    @Override
    public void startResetPasswordActivity() {
        Intent resetPasswordIntent = new Intent(getContext(), ResetPasswordActivity.class);
        startActivity(resetPasswordIntent);
    }

    @Override
    public void finishActivity(boolean success) {
        if (success) {
            getActivity().setResult(Activity.RESULT_OK);
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
    }
}
