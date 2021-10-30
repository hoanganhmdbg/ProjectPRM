package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.DAO.IBrandDao;
import com.project.prm391.shoesstore.DAO.ICategoryDao;
import com.project.prm391.shoesstore.DAO.IGenderDao;
import com.project.prm391.shoesstore.Entity.BankAccountInformation;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Cart;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.StoreInformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lamtu on 2018-03-22.
 */

public class DataCacheService implements IDataCacheService {
    private final ICategoryDao categoryDao;
    private final IBrandDao brandDao;
    private final IGenderDao genderDao;
    private List<Category> categories = new ArrayList<>();
    private List<Brand> brands = new ArrayList<>();
    private List<Gender> genders = new ArrayList<>();
    private Cart cart = new Cart();
    private StoreInformation storeInformation = getDefaultStoreInformation();

    public DataCacheService(ICategoryDao categoryDao, IBrandDao brandDao, IGenderDao genderDao) {
        this.categoryDao = categoryDao;
        this.brandDao = brandDao;
        this.genderDao = genderDao;
    }

    private static StoreInformation getDefaultStoreInformation() {
        StoreInformation info = new StoreInformation();
        info.setName("Shoes Store");
        info.setEmail("contact@shoesstore.com");
        info.setPhoneNumber("0989999999");
        info.setWebsiteUrl("http://shoesstore.com");
        info.setAddress("Đại Học FPT, Khu Công nghệ cao Hòa Lạc, Thạch Thất, Hà Nội");
        info.setLocationLatitude(21.0133737);
        info.setLocationLongitude(105.5251055);
        BankAccountInformation bankAccountInformation = new BankAccountInformation("0123456789", "Shoes Store", "TPBank");
        info.setBankAccountInformation(bankAccountInformation);
        return info;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    private void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public List<Brand> getBrands() {
        return brands;
    }

    private void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @Override
    public List<Gender> getGenders() {
        return genders;
    }

    private void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public StoreInformation getStoreInformation() {
        return storeInformation;
    }

    @Override
    public Task<List<Category>> reloadCategories() {
        return categoryDao.getAllCategories().continueWith(task -> {
            List<Category> categories = task.getResult();
            categories.sort(Comparator.comparing(Category::getName));
            int index = categories.indexOf(new Category("other"));
            if (index != -1) {
                Category category = categories.remove(index);
                categories.add(category);
            }
            setCategories(categories);
            return categories;
        });
    }

    @Override
    public Task<List<Gender>> reloadGender() {
        return genderDao.getAllGenders().continueWith(task -> {
            List<Gender> genders = task.getResult();
            genders.sort(Comparator.comparing(Gender::getName));
            int index = genders.indexOf(new Gender("other"));
            if (index != -1) {
                Gender gender = genders.remove(index);
                genders.add(gender);
            }
            setGenders(genders);
            return genders;
        });
    }

    @Override
    public Task<List<Brand>> reloadBrand() {
        return brandDao.getAllBrands().continueWith(task -> {
            List<Brand> brands = task.getResult();
            brands.sort(Comparator.comparing(Brand::getName));
            int index = brands.indexOf(new Brand("other"));
            if (index != -1) {
                Brand brand = brands.remove(index);
                brands.add(brand);
            }
            setBrands(brands);
            return brands;
        });
    }
}
