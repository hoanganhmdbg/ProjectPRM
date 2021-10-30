package com.project.prm391.shoesstore.Views.SearchResultView;

import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

/**
 * Created by lamtu on 2018-03-11.
 */

public interface SearchResultView {

    void initializeSortSpinner(List<String> sortActions);

    void displayProducts(List<Product> products);

    void showProgressDialog();

    void hideProgressDialog();

    void notifyException(Exception ex);

    void startFilterActivity();

    void startProductDetailActivity(Product product);
}
