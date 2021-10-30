package com.project.prm391.shoesstore.Views.CartView;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.prm391.shoesstore.Adapter.CartAdapter;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Presenter.CartPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Utils.Strings;
import com.project.prm391.shoesstore.Views.HomeView.HomeActivity;
import com.project.prm391.shoesstore.Views.MenuView.MenuActivity;
import com.project.prm391.shoesstore.Views.PaymentView.PlaceOrderActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by duytq on 3/12/2018.
 */

public class CartActivity extends MenuActivity implements CartView, View.OnClickListener {
    private final CartPresenter cartPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvCart)
    RecyclerView rvCart;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.layoutCartEmpty)
    ViewGroup layoutCartEmpty;
    @BindView(R.id.layoutCartNotEmpty)
    ViewGroup layoutCartNotEmpty;
    @BindView(R.id.btnPlaceOrder)
    Button btnPlaceOrder;
    private CartAdapter cartAdapter;

    public CartActivity() {
        this.cartPresenter = new CartPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartPresenter.loadData();
    }

    @Override
    public int getContentViewId() {
        return R.layout.cart_layout;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_cart;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @OnClick(R.id.btnPlaceOrder)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaceOrder:
                cartPresenter.placeOrder();
                break;
        }
    }

    @Override
    public void displayCart(List<Product> products) {
        if (products.isEmpty()) {
            layoutCartEmpty.setVisibility(View.VISIBLE);
            layoutCartNotEmpty.setVisibility(View.GONE);
        } else {
            layoutCartEmpty.setVisibility(View.GONE);
            layoutCartNotEmpty.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            cartAdapter = new CartAdapter(this, products);
            rvCart.setLayoutManager(layoutManager);
            rvCart.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayTotalPrice(double totalPrice) {
        tvTotalPrice.setText(Strings.formatPrice(totalPrice));
    }

    @Override
    public void notifyPlaceOrderRequireSignIn() {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), getString(R.string.msg_place_order_require_sign_in)).show();
    }

    @Override
    public void startPlaceOrderActivity() {
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        startActivity(intent);
    }
}
