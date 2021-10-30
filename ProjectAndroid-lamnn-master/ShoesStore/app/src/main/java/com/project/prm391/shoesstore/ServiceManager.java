package com.project.prm391.shoesstore;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.project.prm391.shoesstore.DAO.BrandDao;
import com.project.prm391.shoesstore.DAO.CategoryDao;
import com.project.prm391.shoesstore.DAO.GenderDao;
import com.project.prm391.shoesstore.DAO.IBrandDao;
import com.project.prm391.shoesstore.DAO.ICategoryDao;
import com.project.prm391.shoesstore.DAO.IGenderDao;
import com.project.prm391.shoesstore.DAO.IOrderDao;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.DAO.IUserDao;
import com.project.prm391.shoesstore.DAO.OrderDao;
import com.project.prm391.shoesstore.DAO.ProductDao;
import com.project.prm391.shoesstore.DAO.UserDao;
import com.project.prm391.shoesstore.Services.DataCacheService;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Services.IOrderService;
import com.project.prm391.shoesstore.Services.IProductService;
import com.project.prm391.shoesstore.Services.IReviewService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Services.IWishListService;
import com.project.prm391.shoesstore.Services.NotificationService;
import com.project.prm391.shoesstore.Services.OrderService;
import com.project.prm391.shoesstore.Services.ProductService;
import com.project.prm391.shoesstore.Services.ReviewService;
import com.project.prm391.shoesstore.Services.UserService;
import com.project.prm391.shoesstore.Services.WishListService;

/**
 * A singleton class to hold all the services used by the application.
 */
public class ServiceManager implements IServiceManager {
    private static final ServiceManager instance = new ServiceManager();
    private boolean initialized = false;
    private Context applicationContext;
    private IBrandDao brandDao;
    private ICategoryDao categoryDao;
    private IGenderDao genderDao;
    private IOrderDao orderDao;
    private IProductDao productDao;
    private IUserDao userDao;
    private IUserService userService;
    private IProductService productService;
    private GoogleSignInClient googleSignInClient;
    private INotificationService notificationService;
    private IDataCacheService dataCacheService;
    private IOrderService orderService;
    private IReviewService reviewService;
    private IWishListService wishListService;

    private ServiceManager() {
    }

    /**
     * Get the default instance of service manager.
     *
     * @return The default instance.
     */
    public static ServiceManager getInstance() {
        return instance;
    }

    private void checkIfInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("Services has not been initialized.");
        }
    }

    @Override
    public void initializeServices(Context applicationContext) {
        if (isInitialized()) {
            throw new IllegalStateException("Services has already been initialized.");
        }
        this.applicationContext = applicationContext;
        // Initialize DAO
        this.brandDao = new BrandDao();
        this.categoryDao = new CategoryDao();
        this.genderDao = new GenderDao();
        this.orderDao = new OrderDao();
        this.productDao = new ProductDao();
        this.userDao = new UserDao();
        // Initialize services
        this.dataCacheService = new DataCacheService(categoryDao, brandDao, genderDao);
        this.userService = new UserService(userDao);
        this.productService = new ProductService(dataCacheService, productDao);
        this.orderService = new OrderService(orderDao, productDao);
        this.reviewService = new ReviewService(productDao, userDao);
        this.wishListService = new WishListService(userService, productDao);
        this.googleSignInClient = buildGoogleSignInClient(applicationContext);
        this.notificationService = new NotificationService(applicationContext);
        FacebookSdk.sdkInitialize(applicationContext);
        // TODO: Initialize services here.

        // Mark as initialized
        this.initialized = true;
    }

    private GoogleSignInClient buildGoogleSignInClient(Context context) {
        String clientId = context.getString(R.string.google_auth_client_id);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(context, options);
        return client;
    }

    @Override
    public void finalizeServices() {
        checkIfInitialized();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Context getApplicationContext() {
        checkIfInitialized();
        return applicationContext;
    }

    @Override
    public IBrandDao getBrandDao() {
        checkIfInitialized();
        return brandDao;
    }

    @Override
    public ICategoryDao getCategoryDao() {
        checkIfInitialized();
        return categoryDao;
    }

    @Override
    public IGenderDao getGenderDao() {
        checkIfInitialized();
        return genderDao;
    }

    @Override
    public IOrderDao getOrderDao() {
        checkIfInitialized();
        return orderDao;
    }

    @Override
    public IProductDao getProductDao() {
        checkIfInitialized();
        return productDao;
    }

    @Override
    public IUserDao getUserDao() {
        checkIfInitialized();
        return userDao;
    }

    @Override
    public IUserService getUserService() {
        checkIfInitialized();
        return userService;
    }

    @Override
    public IOrderService getOrderService() {
        checkIfInitialized();
        return orderService;
    }

    @Override
    public IProductService getProductService() {
        checkIfInitialized();
        return productService;
    }

    @Override
    public IReviewService getReviewService() {
        checkIfInitialized();
        return reviewService;
    }

    @Override
    public IWishListService getWishListService() {
        checkIfInitialized();
        return wishListService;
    }


    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        checkIfInitialized();
        return googleSignInClient;
    }

    @Override
    public INotificationService getNotificationService() {
        checkIfInitialized();
        return notificationService;
    }

    @Override
    public IDataCacheService getDataCacheService() {
        checkIfInitialized();
        return dataCacheService;
    }
}
