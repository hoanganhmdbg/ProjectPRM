package com.project.prm391.shoesstore.Presenter;

import android.util.Log;

import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IProductService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Services.IWishListService;
import com.project.prm391.shoesstore.Views.WishListView.WishListView;

import java.util.ArrayList;
import java.util.List;

public class WishListPresenter {
    private static final String TAG = WishListPresenter.class.getName();

    private final WishListView view;
    private final IWishListService wishListService;
    private final IUserService userService;
    private final IProductService productService;

    private List<Product> cachedWishList = new ArrayList<>();

    public WishListPresenter(WishListView view) {
        this.view = view;
        this.wishListService = ServiceManager.getInstance().getWishListService();
        this.userService = ServiceManager.getInstance().getUserService();
        this.productService = ServiceManager.getInstance().getProductService();
    }

    public void loadWishList() {
        if (userService.isSignedIn()) {
            view.showProgressDialog();
            wishListService
                    .getCurrentUserWishList(true)
                    .addOnCompleteListener(task -> {
                        view.hideProgressDialog();
                        if (task.isSuccessful()) {
                            cachedWishList = productService.sortProducts(task.getResult(), Product.SortField.NAME, true);
                            displayWishList();
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, null, e);
                            view.notifyException(e);
                        }
                    });
        } else {
            view.finishActivity();
        }
    }

    private void displayWishList() {
        view.displayWishList(cachedWishList);
    }
}
