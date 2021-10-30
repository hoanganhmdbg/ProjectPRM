package com.project.prm391.shoesstore.Views.SplashScreenView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Presenter.SplashScreenPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Views.HomeView.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenView {
    private static boolean splashLoaded = false;
    private final SplashScreenPresenter splashScreenPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public SplashScreenActivity() {
        this.splashScreenPresenter = new SplashScreenPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!splashLoaded) {
            setContentView(R.layout.splash_screen_layout);
            ButterKnife.bind(this);
            new Handler().postDelayed(() -> {
                if (!checkIfNetworkConnected()) {
                    showErrorDialog(getString(R.string.msg_network_unconnected));
                    return;
                }
                splashScreenPresenter.loadDataIntoCache();
            }, Constants.SPLASH_SCREEN_LOAD_DELAY);
            splashLoaded = true;
        } else {
            startHomeActivity(false);
        }
    }

    private boolean checkIfNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void showErrorDialog(String error) {
        AlertDialog dialog = Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error);
        dialog.setOnDismissListener(dialog1 -> terminateApp());
        dialog.show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
        terminateApp();
    }

    @Override
    public void startHomeActivity(boolean firstTime) {
        if (firstTime) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void terminateApp() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
