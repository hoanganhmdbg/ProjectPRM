package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

public interface IWishListService {
    Task<List<Product>> getCurrentUserWishList();

    Task<List<Product>> getCurrentUserWishList(boolean fetchProducts);

    Task<Boolean> checkIfProductInCurrentUserWishList(Product product);

    Task<Boolean> addProductToCurrentUserWishList(Product product);

    Task<Boolean> removeProductFromUserWishList(Product product);
}
