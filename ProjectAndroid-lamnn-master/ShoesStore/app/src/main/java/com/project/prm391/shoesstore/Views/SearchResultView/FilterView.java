package com.project.prm391.shoesstore.Views.SearchResultView;

import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.FilterObject;
import com.project.prm391.shoesstore.Entity.Gender;

import java.util.List;

public interface FilterView {

    Category getSelectedCategory();

    Brand getSelectedBrand();

    Gender getSelectedGender();

    double getMinimumPrice();

    double getMaximumPrice();

    void selectCategory(Category category);

    void selectBrand(Brand brand);

    void selectGender(Gender gender);

    void setMinMaxPrice(Double minPrice, Double maxPrice);

    void initCategorySpinner(List<Category> categories);

    void initBrandSpinner(List<Brand> brands);

    void initGenderSpinner(List<Gender> genders);

    void setupFilters(FilterObject filters);

    void finishActivity(boolean success);
}
