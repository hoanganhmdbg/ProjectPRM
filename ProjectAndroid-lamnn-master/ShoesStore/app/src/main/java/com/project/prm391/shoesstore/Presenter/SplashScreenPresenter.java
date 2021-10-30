package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Views.SplashScreenView.SplashScreenView;

/**
 * Created by nguyen on 3/25/2018.
 */

public class SplashScreenPresenter {
    private static final String TAG = SplashScreenPresenter.class.getName();

    private final SplashScreenView view;
    private final IDataCacheService dataCacheService;
    private final Resources resources;

    public SplashScreenPresenter(SplashScreenView view) {
        this.view = view;
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void loadDataIntoCache() {
        view.showProgressBar();
        Tasks.whenAll(
                dataCacheService.reloadCategories(),
                dataCacheService.reloadBrand(),
                dataCacheService.reloadGender()
        ).addOnCompleteListener(task -> {
            view.hideProgressBar();
            if (task.isSuccessful()) {
                view.startHomeActivity(true);
            } else {
                Exception e = task.getException();
                Log.e(TAG, null, e);
                view.notifyException(e);
            }
        });
    }
}
