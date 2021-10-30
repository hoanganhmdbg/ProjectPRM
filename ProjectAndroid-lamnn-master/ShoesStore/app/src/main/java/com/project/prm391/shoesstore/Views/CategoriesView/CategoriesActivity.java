package com.project.prm391.shoesstore.Views.CategoriesView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.project.prm391.shoesstore.Adapter.CategoriesAdapter;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Presenter.CategoriesPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Views.MenuView.MenuActivity;
import com.project.prm391.shoesstore.Views.SearchResultView.SearchResultActivity;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lamtu on 2018-03-14.
 */

public class CategoriesActivity extends MenuActivity implements CategoriesView, CategoriesAdapter.OnMenuItemSelectedListener {
    private final CategoriesPresenter categoriesPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.elvCategories)
    ExpandableListView elvCategories;
    private CategoriesAdapter categoriesAdapter;

    public CategoriesActivity() {
        categoriesPresenter = new CategoriesPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        categoriesPresenter.loadData();
    }

    @Override
    public int getContentViewId() {
        return R.layout.categories_layout;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_category;
    }

    @Override
    public void loadDataToListView(List headers, Map<Object, List> children) {
        categoriesAdapter = new CategoriesAdapter(this, headers, children, this);
        elvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void onMenuItemSelected(Object header, Object child) {
        if (child instanceof Brand) {
            Brand brand = (Brand) child;
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("brand", brand);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (child instanceof Category) {
            Category category = (Category) child;
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("category", category);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (child instanceof Gender) {
            Gender gender = (Gender) child;
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("gender", gender);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
