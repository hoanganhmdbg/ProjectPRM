package com.project.prm391.shoesstore.Views.MenuView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Views.AccountView.AccountActivity;
import com.project.prm391.shoesstore.Views.CartView.CartActivity;
import com.project.prm391.shoesstore.Views.CategoriesView.CategoriesActivity;
import com.project.prm391.shoesstore.Views.HomeView.HomeActivity;

/**
 * Created by lamtu on 2018-03-14.
 */

public abstract class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.navigationView);

        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                //    BottomNavigationViewHelper.disableShiftMode(navigationView);
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));

            } else if (itemId == R.id.navigation_category) {
                //   BottomNavigationViewHelper.disableShiftMode(navigationView);
                startActivity(new Intent(MenuActivity.this, CategoriesActivity.class));
            } else if (itemId == R.id.navigation_cart) {
                //    BottomNavigationViewHelper.disableShiftMode(navigationView);
                startActivity(new Intent(MenuActivity.this, CartActivity.class));
            } else if (itemId == R.id.navigation_account) {
                //    BottomNavigationViewHelper.disableShiftMode(navigationView);
                startActivity(new Intent(MenuActivity.this, AccountActivity.class));

            }
            finish();
        }, 300);

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    public abstract int getContentViewId();

    public abstract int getNavigationMenuItemId();


}
