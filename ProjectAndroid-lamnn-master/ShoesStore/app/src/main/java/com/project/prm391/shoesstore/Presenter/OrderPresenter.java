package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IOrderService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Views.OrderView.OrderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class OrderPresenter {
    private static final String TAG = OrderPresenter.class.getName();
    private static final String[] ORDER_STATUS_FILTERS = {
            "Any status",
            "Completed",
            "Processing",
    };

    private static final String[] ORDER_DATE_FILTERS = {
            "Any time",
            "Within 1 day",
            "Within 1 week",
            "Within 1 month",
            "Within 6 months"
    };

    private final OrderView view;
    private final IUserService userService;
    private final IOrderService orderService;
    private final Resources resources;

    private int statusFilterIndex = 0;
    private int dateFilterIndex = 0;

    private List<Order> cachedOrders = new ArrayList<>();

    public OrderPresenter(OrderView view) {
        this.view = view;
        this.userService = ServiceManager.getInstance().getUserService();
        this.orderService = ServiceManager.getInstance().getOrderService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public void setStatusFilterIndex(int statusFilterIndex) {
        if (statusFilterIndex != this.statusFilterIndex) {
            this.statusFilterIndex = statusFilterIndex;
            filterAndDisplayOrders();
        }
    }

    public void setDateFilterIndex(int dateFilterIndex) {
        if (this.dateFilterIndex != dateFilterIndex) {
            this.dateFilterIndex = dateFilterIndex;
            filterAndDisplayOrders();
        }
    }

    public void initializeFilters() {
        setStatusFilterIndex(0);
        setDateFilterIndex(0);
        view.initializeFilterSpinners(Arrays.asList(ORDER_STATUS_FILTERS), Arrays.asList(ORDER_DATE_FILTERS));
    }

    public void loadOrders() {
        if (userService.isSignedIn()) {
            view.showProgressDialog();
            orderService
                    .getOrdersByUserWithOrderItems(userService.getCurrentUserProfile())
                    .addOnCompleteListener(task -> {
                        view.hideProgressDialog();
                        if (task.isSuccessful()) {
                            cachedOrders = task.getResult();
                            filterAndDisplayOrders();
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, null, e);
                            view.notifyException(e);
                        }
                    });
        } else {
            view.finishActivity();
        }
    }

    public void filterAndDisplayOrders() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Stream<Order> stream = cachedOrders.stream();
        switch (statusFilterIndex) {
            case 1:
                stream = stream.filter(order -> order.getStatus().isCompleted());
                break;
            case 2:
                stream = stream.filter(order -> !order.getStatus().isCompleted());
                break;
        }
        switch (dateFilterIndex) {
            case 1:
                calendar.add(Calendar.DATE, -1);
                stream = stream.filter(order -> order.getTimestamp().after(calendar.getTime()));
                break;
            case 2:
                calendar.add(Calendar.WEEK_OF_MONTH, -1);
                stream = stream.filter(order -> order.getTimestamp().after(calendar.getTime()));
                break;
            case 3:
                calendar.add(Calendar.MONTH, -1);
                stream = stream.filter(order -> order.getTimestamp().after(calendar.getTime()));
                break;
            case 4:
                calendar.add(Calendar.MONTH, -6);
                stream = stream.filter(order -> order.getTimestamp().after(calendar.getTime()));
                break;
        }
        List<Order> filteredOrders = stream.collect(Collectors.toList());
        view.displayOrders(filteredOrders);
    }
}
