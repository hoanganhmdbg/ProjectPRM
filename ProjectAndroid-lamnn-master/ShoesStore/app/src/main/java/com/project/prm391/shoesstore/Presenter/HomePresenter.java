package com.project.prm391.shoesstore.Presenter;

import android.util.Log;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.IProductService;
import com.project.prm391.shoesstore.Views.HomeView.HomeView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lamtu on 2018-02-27.
 */

public class HomePresenter {
    private static final String TAG = HomePresenter.class.getName();

    private final HomeView view;
    private final IProductService productService;
    private final IDataCacheService dataCacheService;

    public HomePresenter(HomeView view) {
        this.view = view;
        this.productService = ServiceManager.getInstance().getProductService();
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
    }

    public void showBrands() {
        view.showBrands(dataCacheService.getBrands());
    }

    public void loadProducts() {
        view.toggleNewestProductsProgressBar(true);
        view.toggleSaleProductsProgressBar(true);
        view.toggleTopRatingProductsProgressBar(true);

        productService.getAllProducts().addOnCompleteListener(task -> {
            view.toggleRefreshing(false);
            view.toggleNewestProductsProgressBar(false);
            view.toggleSaleProductsProgressBar(false);
            view.toggleTopRatingProductsProgressBar(false);

            if (task.isSuccessful()) {
                List<Product> products = task.getResult();
                view.showNewestProducts(filterNewestProducts(products));
                view.showSaleProducts(filterSaleProducts(products));
                view.showTopRatingProducts(filterTopRatingProducts(products));
            } else {
                Exception e = task.getException();
                Log.e(TAG, null, e);
                view.notifyException(e);
                view.showNewestProducts(new ArrayList<>());
                view.showSaleProducts(new ArrayList<>());
                view.showTopRatingProducts(new ArrayList<>());
            }
        });

    }

    private List<Product> filterTopRatingProducts(List<Product> products) {
        return products
                .stream()
                .sorted(Product.getComparator(Product.SortField.AVERAGE_RATINGS, false))
                .limit(Constants.PRODUCT_LIST_MAX_ITEMS)
                .collect(Collectors.toList());
    }

    private List<Product> filterNewestProducts(List<Product> products) {
        return products
                .stream()
                .sorted(Product.getComparator(Product.SortField.POSTED_TIME, false))
                .limit(Constants.PRODUCT_LIST_MAX_ITEMS)
                .collect(Collectors.toList());
    }

    private List<Product> filterSaleProducts(List<Product> products) {
        return products
                .stream()
                .filter(product -> product.isAvailable() && product.getDiscountRate() > 0)
                .sorted(Product.getComparator(Product.SortField.DISCOUNT_RATE, false))
                .limit(Constants.PRODUCT_LIST_MAX_ITEMS)
                .collect(Collectors.toList());
    }
}


