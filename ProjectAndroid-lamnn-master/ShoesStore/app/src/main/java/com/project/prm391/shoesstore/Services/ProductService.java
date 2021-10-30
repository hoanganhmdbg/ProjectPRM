package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Utils.Strings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nguyen on 3/23/2018.
 */

public class ProductService implements IProductService {
    private final IDataCacheService IDataCacheService;
    private final IProductDao productDao;

    public ProductService(IDataCacheService IDataCacheService, IProductDao productDao) {
        this.IDataCacheService = IDataCacheService;
        this.productDao = productDao;
    }

    @Override
    public Task<List<Product>> getAllProducts() {
        return productDao.getAllProducts()
                .continueWith(task -> task.getResult()
                        .stream()
                        .peek(product -> mergeProductFieldsFromCache(product))
                        .collect(Collectors.toList()));
    }

    @Override
    public Task<Product> getProductById(String id) {
        return productDao.getProductById(id)
                .continueWith(task -> mergeProductFieldsFromCache(task.getResult()));
    }

    /**
     * Search products that meet a number of conditions, then sort them.
     *
     * @param keyword   The search keyword.
     * @param category  The category filter.
     * @param brand     The brand filter.
     * @param gender    The gender filter.
     * @param minPrice  The minimum price filter.
     * @param maxPrice  The maximum price filter.
     * @param sortField The field to sort the products.
     * @param ascending True if sort in ascending order, false if in descending order.
     * @return A task that returns the list of sorted products that match the conditions.
     */
    @Override
    public Task<List<Product>> searchProducts(String keyword, Category category, Brand brand, Gender gender, Double minPrice, Double maxPrice, Product.SortField sortField, boolean ascending) {
        return getAllProducts()
                .continueWith(task -> filterProducts(task.getResult(), keyword, category, brand, gender, minPrice, maxPrice))
                .continueWith(task -> sortProducts(task.getResult(), sortField, ascending));
    }

    /**
     * Filter products that meet a number of conditions.
     *
     * @param products The original list of products.
     * @param keyword  The search keyword.
     * @param category The category filter.
     * @param brand    The brand filter.
     * @param gender   The gender filter.
     * @param minPrice The minimum price filter.
     * @param maxPrice The maximum price filter.
     * @return The filtered list of products.
     */
    @Override
    public List<Product> filterProducts(List<Product> products, String keyword, Category category, Brand brand, Gender gender, Double minPrice, Double maxPrice) {
        Stream<Product> stream = products.stream();
        if (keyword != null && !keyword.isEmpty()) {
            stream = stream.filter(product -> Strings.containsIgnoreCase(product.getName(), keyword));
        }
        if (category != null) {
            stream = stream.filter(product -> category.equals(product.getCategory()));
        }
        if (brand != null) {
            stream = stream.filter(product -> brand.equals(product.getBrand()));
        }
        if (gender != null) {
            stream = stream.filter(product -> gender.equals(product.getGender()));
        }
        if (minPrice != null) {
            stream = stream.filter(product -> product.getCurrentPrice() >= minPrice);
        }
        if (maxPrice != null) {
            stream = stream.filter(product -> product.getCurrentPrice() <= maxPrice);
        }
        return stream.collect(Collectors.toList());
    }


    /**
     * Sort products.
     *
     * @param products  The original list of products.
     * @param sortField The field to sort the products.
     * @param ascending True if sort in ascending order, false if in descending order.
     * @return The sorted list of products.
     */
    @Override
    public List<Product> sortProducts(List<Product> products, Product.SortField sortField, boolean ascending) {
        Stream<Product> stream = products.stream();
        if (sortField != null) {
            stream = stream.sorted(Product.getComparator(sortField, ascending));
        }
        return stream.collect(Collectors.toList());
    }

    private Product mergeProductFieldsFromCache(Product product) {
        IDataCacheService
                .getCategories()
                .stream()
                .filter(category -> category.equals(product.getCategory()))
                .findFirst()
                .ifPresent(c -> product.setCategory(c));
        IDataCacheService
                .getBrands()
                .stream()
                .filter(brand -> brand.equals(product.getBrand()))
                .findFirst()
                .ifPresent(brand -> product.setBrand(brand));
        IDataCacheService
                .getGenders()
                .stream()
                .filter(gender -> gender.equals(product.getGender()))
                .findFirst()
                .ifPresent(gender -> product.setGender(gender));
        return product;
    }


}
