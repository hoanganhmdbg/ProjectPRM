package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * Created by lamtu on 2018-03-25.
 */

public class FilterObject implements Serializable {
    private Category category;
    private Brand brand;
    private Gender gender;
    private Double minimumPrice;
    private Double maximumPrice;

    public FilterObject() {
    }

    public FilterObject(Category category, Brand brand, Gender gender, Double minimumPrice, Double maximumPrice) {
        this.category = category;
        this.brand = brand;
        this.gender = gender;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Double getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(Double maximumPrice) {
        this.maximumPrice = maximumPrice;
    }
}
