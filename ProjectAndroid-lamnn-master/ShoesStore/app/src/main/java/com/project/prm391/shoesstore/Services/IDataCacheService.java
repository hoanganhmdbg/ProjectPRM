package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Cart;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.StoreInformation;

import java.util.List;

/**
 * Created by nguyen on 3/23/2018.
 */

public interface IDataCacheService {
    List<Category> getCategories();

    List<Brand> getBrands();

    List<Gender> getGenders();

    Cart getCart();

    StoreInformation getStoreInformation();

    Task<List<Category>> reloadCategories();

    Task<List<Gender>> reloadGender();

    Task<List<Brand>> reloadBrand();
}
