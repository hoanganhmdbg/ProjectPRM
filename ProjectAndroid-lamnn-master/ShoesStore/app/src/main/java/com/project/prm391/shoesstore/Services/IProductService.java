package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.Product;

import java.util.List;

/**
 * Created by nguyen on 3/23/2018.
 */

public interface IProductService {
    Task<List<Product>> getAllProducts();

    Task<Product> getProductById(String id);

    Task<List<Product>> searchProducts(String keyword, Category category, Brand brand, Gender gender, Double minPrice, Double maxPrice, Product.SortField sortField, boolean ascending);

    List<Product> filterProducts(List<Product> products, String keyword, Category category, Brand brand, Gender gender, Double minPrice, Double maxPrice);

    List<Product> sortProducts(List<Product> products, Product.SortField sortField, boolean ascending);
}
