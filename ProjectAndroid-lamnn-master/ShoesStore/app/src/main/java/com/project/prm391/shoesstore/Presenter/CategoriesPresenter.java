package com.project.prm391.shoesstore.Presenter;

import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Views.CategoriesView.CategoriesView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CategoriesPresenter {

    private final IDataCacheService dataCacheService;
    private CategoriesView categoriesView;

    public CategoriesPresenter(CategoriesView categoriesView) {
        this.categoriesView = categoriesView;
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
    }

    public void loadData() {
        Map<Object, List> map = new LinkedHashMap<>();
        map.put("Brands", dataCacheService.getBrands());
        map.put("Categories", dataCacheService.getCategories());
        map.put("Genders/Ages", dataCacheService.getGenders());
        categoriesView.loadDataToListView(new ArrayList<>(map.keySet()), map);
    }

}
