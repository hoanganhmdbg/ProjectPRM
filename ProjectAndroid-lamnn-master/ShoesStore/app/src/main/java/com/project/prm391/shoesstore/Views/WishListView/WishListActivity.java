package com.project.prm391.shoesstore.Views.WishListView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.prm391.shoesstore.Adapter.VerticalListProductAdapter;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Presenter.WishListPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Views.ProductDetailView.ProductDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishListActivity extends AppCompatActivity implements WishListView {
    private final WishListPresenter wishListPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvWishList)
    RecyclerView rvWishList;
    @BindView(R.id.layoutWishListEmpty)
    ViewGroup layoutWishListEmpty;
    @BindView(R.id.layoutWishListNotEmpty)
    ViewGroup layoutWishListNotEmpty;
    private ProgressDialog progressDialog;

    public WishListActivity() {
        this.wishListPresenter = new WishListPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wish_list_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        wishListPresenter.loadWishList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mitRefresh) {
            wishListPresenter.loadWishList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_my_wish_list), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void notifyException(Exception e) {
        notificationService.displayToastForException(e);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void displayWishList(List<Product> wishList) {
        if (wishList.isEmpty()) {
            layoutWishListEmpty.setVisibility(View.VISIBLE);
            layoutWishListNotEmpty.setVisibility(View.GONE);
        } else {
            layoutWishListEmpty.setVisibility(View.GONE);
            layoutWishListNotEmpty.setVisibility(View.VISIBLE);

            VerticalListProductAdapter adapter = new VerticalListProductAdapter(this, wishList, this::startProductDetailActivity);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvWishList.setLayoutManager(layoutManager);
            rvWishList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void startProductDetailActivity(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }
}
