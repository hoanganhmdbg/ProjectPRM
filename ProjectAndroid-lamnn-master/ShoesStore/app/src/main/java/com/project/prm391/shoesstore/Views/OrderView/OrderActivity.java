package com.project.prm391.shoesstore.Views.OrderView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.prm391.shoesstore.Adapter.OrderAdapter;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Presenter.OrderPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;


public class OrderActivity extends AppCompatActivity implements OrderView, AdapterView.OnItemSelectedListener {
    private final OrderPresenter orderPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spnOrderStatusFilter)
    Spinner spnOrderStatusFilter;
    @BindView(R.id.spnOrderDateFilter)
    Spinner spnOrderDateFilter;
    @BindView(R.id.layoutOrderListEmpty)
    ViewGroup layoutOrderListEmpty;
    @BindView(R.id.layoutOrderListNotEmpty)
    ViewGroup layoutOrderListNotEmpty;
    @BindView(R.id.rvOrders)
    RecyclerView rvOrders;
    private ProgressDialog progressDialog;

    public OrderActivity() {
        this.orderPresenter = new OrderPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        orderPresenter.initializeFilters();
        orderPresenter.loadOrders();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mitRefresh) {
            orderPresenter.loadOrders();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initializeFilterSpinners(List<String> statusFilters, List<String> dateFilters) {
        ArrayAdapter<String> statusFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusFilters);
        statusFilterAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnOrderStatusFilter.setAdapter(statusFilterAdapter);

        ArrayAdapter<String> dateFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateFilters);
        dateFilterAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnOrderDateFilter.setAdapter(dateFilterAdapter);
    }

    @Override
    public void displayOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            layoutOrderListEmpty.setVisibility(View.VISIBLE);
            layoutOrderListNotEmpty.setVisibility(View.GONE);
        } else {
            layoutOrderListEmpty.setVisibility(View.GONE);
            layoutOrderListNotEmpty.setVisibility(View.VISIBLE);
            OrderAdapter orderAdapter = new OrderAdapter(this, orders);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rvOrders.setLayoutManager(layoutManager);
            rvOrders.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_my_orders), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @OnItemSelected({R.id.spnOrderStatusFilter, R.id.spnOrderDateFilter})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnOrderStatusFilter:
                orderPresenter.setStatusFilterIndex(position);
                break;
            case R.id.spnOrderDateFilter:
                orderPresenter.setDateFilterIndex(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
