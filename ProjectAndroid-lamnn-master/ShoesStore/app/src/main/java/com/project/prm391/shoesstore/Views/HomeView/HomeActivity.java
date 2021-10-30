package com.project.prm391.shoesstore.Views.HomeView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.prm391.shoesstore.Adapter.BrandAdapter;
import com.project.prm391.shoesstore.Adapter.HorizontalListProductAdapter;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Presenter.HomePresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Views.MenuView.MenuActivity;
import com.project.prm391.shoesstore.Views.ProductDetailView.ProductDetailActivity;
import com.project.prm391.shoesstore.Views.SearchResultView.SearchResultActivity;
import com.project.prm391.shoesstore.Views.SearchView.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lamtu on 2018-02-27.
 */

public class HomeActivity extends MenuActivity implements HomeView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final HomePresenter homePresenter;
    private final INotificationService notificationService;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.rvBrands)
    RecyclerView rvBrands;
    @BindView(R.id.rvNewestProducts)
    RecyclerView rvNewestProducts;
    @BindView(R.id.rvTopRatingProducts)
    RecyclerView rvTopRatingProducts;
    @BindView(R.id.rvSaleProducts)
    RecyclerView rvSaleProducts;
    @BindView(R.id.tvEmptyBrands)
    TextView tvEmptyBrands;
    @BindView(R.id.tvEmptyNewestProducts)
    TextView tvEmptyNewestProducts;
    @BindView(R.id.tvEmptySaleProducts)
    TextView tvEmptySaleProducts;
    @BindView(R.id.tvEmptyTopRatingProducts)
    TextView tvEmptyTopRatingProducts;
    @BindView(R.id.pbNewestProducts)
    ProgressBar pbNewestProducts;
    @BindView(R.id.pbTopRatingProducts)
    ProgressBar pbTopRatingProducts;
    @BindView(R.id.pbSaleProducts)
    ProgressBar pbSaleProducts;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout layoutSwipeRefresh;

    public HomeActivity() {
        this.homePresenter = new HomePresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    public int getContentViewId() {
        return R.layout.home_layout;
    }


    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        layoutSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        homePresenter.showBrands();
        homePresenter.loadProducts();
    }

    @OnClick(R.id.btnSearch)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSearch: {
                startSearchActivity();
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        homePresenter.loadProducts();
    }

    @Override
    public void showBrands(List<Brand> brands) {
        if (brands == null || brands.isEmpty()) {
            tvEmptyBrands.setVisibility(View.VISIBLE);
            rvBrands.setVisibility(View.GONE);
        } else {
            tvEmptyBrands.setVisibility(View.GONE);
            rvBrands.setVisibility(View.VISIBLE);
            BrandAdapter adapter = new BrandAdapter(this, brands, this::startResultSearchActivityForBrand);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
            rvBrands.setLayoutManager(layoutManager);
            rvBrands.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void showProductOnView(List<Product> products, RecyclerView recyclerView, TextView emptyDataTextView) {
        if (products == null || products.isEmpty()) {
            emptyDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            HorizontalListProductAdapter adapter = new HorizontalListProductAdapter(this, products, this::startProductDetailActivity);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showTopRatingProducts(List<Product> products) {
        showProductOnView(products, rvTopRatingProducts, tvEmptyTopRatingProducts);
    }

    @Override
    public void showSaleProducts(List<Product> products) {
        showProductOnView(products, rvSaleProducts, tvEmptySaleProducts);
    }

    @Override
    public void showNewestProducts(List<Product> products) {
        showProductOnView(products, rvNewestProducts, tvEmptyNewestProducts);
    }

    @Override
    public void toggleTopRatingProductsProgressBar(boolean show) {
        pbTopRatingProducts.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleSaleProductsProgressBar(boolean show) {
        pbSaleProducts.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleNewestProductsProgressBar(boolean show) {
        pbNewestProducts.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void startResultSearchActivityForBrand(Brand brand) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("brand", brand);
        startActivity(intent);
    }

    @Override
    public void startProductDetailActivity(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void toggleRefreshing(boolean refreshing) {
        layoutSwipeRefresh.setRefreshing(refreshing);
    }
}