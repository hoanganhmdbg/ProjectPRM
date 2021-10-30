package com.project.prm391.shoesstore;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.project.prm391.shoesstore.DAO.IBrandDao;
import com.project.prm391.shoesstore.DAO.ICategoryDao;
import com.project.prm391.shoesstore.DAO.IGenderDao;
import com.project.prm391.shoesstore.DAO.IOrderDao;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.DAO.IUserDao;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Services.IOrderService;
import com.project.prm391.shoesstore.Services.IProductService;
import com.project.prm391.shoesstore.Services.IReviewService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Services.IWishListService;

public interface IServiceManager {
    void initializeServices(Context applicationContext);

    void finalizeServices();

    boolean isInitialized();

    Context getApplicationContext();

    IBrandDao getBrandDao();

    ICategoryDao getCategoryDao();

    IGenderDao getGenderDao();

    IOrderDao getOrderDao();

    IProductDao getProductDao();

    IUserDao getUserDao();

    IUserService getUserService();

    IOrderService getOrderService();

    IProductService getProductService();

    IReviewService getReviewService();

    IWishListService getWishListService();

    GoogleSignInClient getGoogleSignInClient();

    INotificationService getNotificationService();

    IDataCacheService getDataCacheService();
}
