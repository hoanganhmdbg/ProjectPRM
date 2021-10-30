package com.project.prm391.shoesstore.Views.CartView;

import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

/**
 * Created by duytq on 3/12/2018.
 */

public interface CartView {

    void displayCart(List<Product> products);

    void displayTotalPrice(double totalPrice);

    void notifyPlaceOrderRequireSignIn();

    void startPlaceOrderActivity();

}
