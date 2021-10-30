package com.project.prm391.shoesstore.Views.OrderView;

import com.project.prm391.shoesstore.Entity.Order;

import java.util.List;

/**
 * Created by duytq on 3/18/2018.
 */

public interface OrderView {

    void initializeFilterSpinners(List<String> statusFilters, List<String> dateFilters);

    void displayOrders(List<Order> orders);

    void showProgressDialog();

    void hideProgressDialog();

    void notifyException(Exception ex);

    void finishActivity();
}
