package com.project.prm391.shoesstore.Presenter;

import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.FilterObject;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Views.SearchResultView.FilterView;

import java.util.ArrayList;
import java.util.List;

public class FilterPresenter {

    private final IDataCacheService dataCacheService;
    private final FilterView view;
    private FilterObject filters = new FilterObject();

    public FilterPresenter(FilterView view) {
        this.view = view;
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
    }

    public FilterObject getFilters() {
        return filters;
    }

    public void setFilters(FilterObject filters) {
        this.filters = filters;
        view.setupFilters(filters);
    }

    public void initializeSpinners() {
        view.initCategorySpinner(getCategoriesForSpinner());
        view.initBrandSpinner(getBrandsForSpinner());
        view.initGenderSpinner(getGendersForSpinner());
    }

    private List<Category> getCategoriesForSpinner() {
        List<Category> categories = new ArrayList<>(dataCacheService.getCategories());
        Category category = new Category();
        category.setId(null);
        category.setName("Any");
        categories.add(0, category);
        return categories;
    }

    private List<Brand> getBrandsForSpinner() {
        List<Brand> brands = new ArrayList<>(dataCacheService.getBrands());
        Brand brand = new Brand();
        brand.setId(null);
        brand.setName("Any");
        brand.setImageUrl(null);
        brands.add(0, brand);
        return brands;
    }

    private List<Gender> getGendersForSpinner() {
        List<Gender> genders = new ArrayList<>(dataCacheService.getGenders());
        Gender gender = new Gender();
        gender.setId(null);
        gender.setName("Any");
        genders.add(0, gender);
        return genders;
    }

    public void acceptFilters() {
        filters.setCategory(view.getSelectedCategory());
        filters.setBrand(view.getSelectedBrand());
        filters.setGender(view.getSelectedGender());
        filters.setMinimumPrice(view.getMinimumPrice());
        filters.setMaximumPrice(view.getMaximumPrice());
        view.finishActivity(true);
    }
}
