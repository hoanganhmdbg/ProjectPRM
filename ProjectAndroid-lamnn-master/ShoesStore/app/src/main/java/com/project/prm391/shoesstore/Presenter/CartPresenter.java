package com.project.prm391.shoesstore.Presenter;

import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Views.CartView.CartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duytq on 3/12/2018.
 */

public class CartPresenter {

    private final CartView view;
    private final IDataCacheService dataCacheService;
    private final IUserService userService;

    public CartPresenter(CartView view) {
        this.view = view;
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void placeOrder() {
        if (userService.isSignedIn()) {
            view.startPlaceOrderActivity();
        } else {
            view.notifyPlaceOrderRequireSignIn();
        }
    }

    public void loadData() {
        List<Product> products = new ArrayList<>(dataCacheService.getCart().getProducts());
        view.displayCart(products);
        view.displayTotalPrice(dataCacheService.getCart().getTotalPrice());
    }

}
