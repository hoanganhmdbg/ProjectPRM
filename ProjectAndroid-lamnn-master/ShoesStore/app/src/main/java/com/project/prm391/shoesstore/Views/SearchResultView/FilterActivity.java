package com.project.prm391.shoesstore.Views.SearchResultView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.FilterObject;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Presenter.FilterPresenter;
import com.project.prm391.shoesstore.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lamtu on 2018-03-19.
 */

public class FilterActivity extends AppCompatActivity implements FilterView, View.OnClickListener, OnRangeSeekbarChangeListener {
    private final FilterPresenter filterPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.spnCategory)
    Spinner spnCategory;
    @BindView(R.id.spnBrand)
    Spinner spnBrand;
    @BindView(R.id.spnGender)
    Spinner spnGender;
    @BindView(R.id.rsbPriceRange)
    CrystalRangeSeekbar rsbPriceRange;
    @BindView(R.id.tvMinPrice)
    TextView tvMinPrice;
    @BindView(R.id.tvMaxPrice)
    TextView tvMaxPrice;

    public FilterActivity() {
        this.filterPresenter = new FilterPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        filterPresenter.initializeSpinners();
        rsbPriceRange.setOnRangeSeekbarChangeListener(this);

        Intent intent = getIntent();
        FilterObject filters = (FilterObject) intent.getSerializableExtra("filters");
        if (filters == null) {
            finishActivity(false);
        } else {
            filterPresenter.setFilters(filters);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnAccept)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAccept:
                filterPresenter.acceptFilters();
                break;
        }
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        tvMinPrice.setText(minValue.toString());
        tvMaxPrice.setText(maxValue.toString());
    }

    @Override
    public Category getSelectedCategory() {
        Category category = (Category) spnCategory.getSelectedItem();
        return category.getId() == null ? null : category;
    }

    @Override
    public Brand getSelectedBrand() {
        Brand brand = (Brand) spnBrand.getSelectedItem();
        return brand.getId() == null ? null : brand;
    }

    @Override
    public Gender getSelectedGender() {
        Gender gender = (Gender) spnGender.getSelectedItem();
        return gender.getId() == null ? null : gender;
    }

    @Override
    public double getMinimumPrice() {
        return rsbPriceRange.getSelectedMinValue().doubleValue();
    }

    @Override
    public double getMaximumPrice() {
        return rsbPriceRange.getSelectedMaxValue().doubleValue();
    }

    @Override
    public void selectCategory(Category category) {
        if (category == null) {
            spnCategory.setSelection(0);
        } else {
            for (int i = 0; i < spnCategory.getCount(); i++) {
                if (spnCategory.getItemAtPosition(i).equals(category)) {
                    spnCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public void selectBrand(Brand brand) {
        if (brand == null) {
            spnBrand.setSelection(0);
        } else {
            for (int i = 0; i < spnBrand.getCount(); i++) {
                if (spnBrand.getItemAtPosition(i).equals(brand)) {
                    spnBrand.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public void selectGender(Gender gender) {
        if (gender == null) {
            spnGender.setSelection(0);
        } else {
            for (int i = 0; i < spnGender.getCount(); i++) {
                if (spnGender.getItemAtPosition(i).equals(gender)) {
                    spnGender.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public void setMinMaxPrice(Double minPrice, Double maxPrice) {
        if (minPrice != null) {
            rsbPriceRange.setMinStartValue(minPrice.floatValue());
        }
        if (maxPrice != null) {
            rsbPriceRange.setMaxStartValue(maxPrice.floatValue());
        }
        rsbPriceRange.apply();
    }

    @Override
    public void initCategorySpinner(List<Category> categories) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    @Override
    public void initBrandSpinner(List<Brand> brands) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brands);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapter);
    }

    @Override
    public void initGenderSpinner(List<Gender> genders) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(adapter);
    }

    @Override
    public void setupFilters(FilterObject filters) {
        selectCategory(filters.getCategory());
        selectBrand(filters.getBrand());
        selectGender(filters.getGender());
        setMinMaxPrice(filters.getMinimumPrice(), filters.getMaximumPrice());
    }

    @Override
    public void finishActivity(boolean success) {
        if (success) {
            Intent intent = new Intent();
            intent.putExtra("filters", filterPresenter.getFilters());
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}

