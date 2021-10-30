package com.project.prm391.shoesstore.Presenter;

import android.util.Log;

import com.project.prm391.shoesstore.Entity.FilterObject;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IProductService;
import com.project.prm391.shoesstore.Views.SearchResultView.SearchResultView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lamtu on 2018-03-11.
 */

public class SearchResultPresenter {
    private static final String[] SORT_ACTIONS = {
            "A to Z",
            "Z to A",
            "Price - Low to High",
            "Price - High to Low",
            "Oldest to Newest",
            "Newest to Oldest",
            "Rating"
    };
    private static final String TAG = SearchResultPresenter.class.getName();

    private final SearchResultView view;
    private final IProductService productService;

    private String keyword;
    private FilterObject filters = new FilterObject();
    private Product.SortField sortField = Product.SortField.NAME;
    private boolean ascending = true;
    private List<Product> cachedProducts = new ArrayList<>();

    public SearchResultPresenter(SearchResultView view) {
        this.view = view;
        this.productService = ServiceManager.getInstance().getProductService();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public FilterObject getFilters() {
        return filters;
    }

    public void setFilters(FilterObject filters) {
        this.filters = filters;
    }

    public void setSortActionIndex(int actionIndex) {
        switch (actionIndex) {
            case 0:
                this.sortField = Product.SortField.NAME;
                this.ascending = true;
                break;
            case 1:
                this.sortField = Product.SortField.NAME;
                this.ascending = false;
                break;
            case 2:
                this.sortField = Product.SortField.CURRENT_PRICE;
                this.ascending = true;
                break;
            case 3:
                this.sortField = Product.SortField.CURRENT_PRICE;
                this.ascending = false;
                break;
            case 4:
                this.sortField = Product.SortField.POSTED_TIME;
                this.ascending = true;
                break;
            case 5:
                this.sortField = Product.SortField.POSTED_TIME;
                this.ascending = false;
                break;
            case 6:
                this.sortField = Product.SortField.AVERAGE_RATINGS;
                this.ascending = false;
                break;
        }
    }

    public void initializeSortActions() {
        setSortActionIndex(0);
        view.initializeSortSpinner(Arrays.asList(SORT_ACTIONS));
    }

    public void searchProducts() {
        view.showProgressDialog();
        productService.searchProducts(
                keyword,
                filters.getCategory(),
                filters.getBrand(),
                filters.getGender(),
                filters.getMinimumPrice(),
                filters.getMaximumPrice(),
                sortField,
                ascending)
                .addOnCompleteListener(task -> {
                    view.hideProgressDialog();
                    if (task.isSuccessful()) {
                        this.cachedProducts = task.getResult();
                    } else {
                        this.cachedProducts = new ArrayList<>();
                        Exception e = task.getException();
                        Log.e(TAG, null, e);
                        view.notifyException(e);
                    }
                    view.displayProducts(this.cachedProducts);
                });
    }

    public void sortProducts() {
        this.cachedProducts = productService.sortProducts(this.cachedProducts, sortField, ascending);
        view.displayProducts(this.cachedProducts);
    }
}
