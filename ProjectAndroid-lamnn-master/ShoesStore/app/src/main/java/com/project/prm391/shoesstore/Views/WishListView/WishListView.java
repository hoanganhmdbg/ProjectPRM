package com.project.prm391.shoesstore.Views.WishListView;

import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

public interface WishListView {
    void showProgressDialog();

    void hideProgressDialog();

    void notifyException(Exception e);

    void finishActivity();

    void displayWishList(List<Product> wishList);
}
