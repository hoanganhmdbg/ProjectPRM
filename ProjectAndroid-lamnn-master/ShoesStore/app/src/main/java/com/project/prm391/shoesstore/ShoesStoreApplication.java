package com.project.prm391.shoesstore;

import android.app.Application;

public class ShoesStoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!ServiceManager.getInstance().isInitialized()) {
            ServiceManager.getInstance().initializeServices(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (ServiceManager.getInstance().isInitialized()) {
            ServiceManager.getInstance().finalizeServices();
        }
    }
}
