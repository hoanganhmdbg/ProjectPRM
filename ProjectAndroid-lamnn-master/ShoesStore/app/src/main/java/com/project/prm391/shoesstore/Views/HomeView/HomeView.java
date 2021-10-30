package com.project.prm391.shoesstore.Views.HomeView;

import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

/**
 * Created by lamtu on 2018-03-06.
 */

public interface HomeView {

    void showBrands(List<Brand> brands);

    void showTopRatingProducts(List<Product> products);

    void showSaleProducts(List<Product> products);

    void showNewestProducts(List<Product> products);

    void toggleTopRatingProductsProgressBar(boolean show);

    void toggleSaleProductsProgressBar(boolean show);

    void toggleNewestProductsProgressBar(boolean show);

    void startSearchActivity();

    void startResultSearchActivityForBrand(Brand brand);

    void startProductDetailActivity(Product product);

    void notifyException(Exception ex);

    void toggleRefreshing(boolean refreshing);
}
