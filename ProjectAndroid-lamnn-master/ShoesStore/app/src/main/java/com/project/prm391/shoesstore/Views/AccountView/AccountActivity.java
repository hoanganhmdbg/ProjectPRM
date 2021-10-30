package com.project.prm391.shoesstore.Views.AccountView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.prm391.shoesstore.Adapter.IconMenuAdapter;
import com.project.prm391.shoesstore.Configs.RequestCode;
import com.project.prm391.shoesstore.Presenter.AccountPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Views.LoginRegisterView.LoginActivity;
import com.project.prm391.shoesstore.Views.LoginRegisterView.RegisterActivity;
import com.project.prm391.shoesstore.Views.MenuView.MenuActivity;
import com.project.prm391.shoesstore.Views.OrderView.OrderActivity;
import com.project.prm391.shoesstore.Views.ProfileView.ProfileDisplayActivity;
import com.project.prm391.shoesstore.Views.StoreInformationView.StoreInformationActivity;
import com.project.prm391.shoesstore.Views.WishListView.WishListActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class AccountActivity extends MenuActivity implements AccountView, View.OnClickListener, ListView.OnItemClickListener {
    private final AccountPresenter accountPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cvLoginRegister)
    CardView vgLoginRegister;
    @BindView(R.id.cvAccountStatus)
    CardView vgAccountStatus;
    @BindView(R.id.cvAccountMenu)
    CardView vgAccountMenu;
    @BindView(R.id.cvCommonMenu)
    CardView vgCommonMenu;
    @BindView(R.id.tvAccountStatus)
    TextView tvAccountStatus;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.lvAccountMenu)
    ListView lvAccountMenu;
    @BindView(R.id.lvCommonMenu)
    ListView lvCommonMenu;

    public AccountActivity() {
        accountPresenter = new AccountPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initializeAccountMenu();
        initializeCommonMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        accountPresenter.checkSignInStatus();
    }

    @Override
    public int getContentViewId() {
        return R.layout.account_layout;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_account;
    }

    private void initializeAccountMenu() {
        List<IconMenuAdapter.MenuItem> menuItems = Arrays.asList(
                new IconMenuAdapter.MenuItem(R.drawable.ic_person_outline_black, getString(R.string.label_my_profile)),
                new IconMenuAdapter.MenuItem(R.drawable.ic_receipt_black, getString(R.string.label_my_orders)),
                new IconMenuAdapter.MenuItem(R.drawable.ic_favorite_border_black, getString(R.string.label_my_wish_list))
        );
        IconMenuAdapter adapter = new IconMenuAdapter(this, menuItems);
        lvAccountMenu.setAdapter(adapter);
    }

    private void initializeCommonMenu() {
        List<IconMenuAdapter.MenuItem> menuItems = Arrays.asList(
                new IconMenuAdapter.MenuItem(R.drawable.ic_info_outline_black, getString(R.string.label_store_information))
        );
        IconMenuAdapter adapter = new IconMenuAdapter(this, menuItems);
        lvCommonMenu.setAdapter(adapter);
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister, R.id.btnLogout})
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLogin:
                startLoginActivity();
                break;
            case R.id.btnRegister:
                startRegisterActivity();
                break;
            case R.id.btnLogout:
                accountPresenter.logout();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.LOGIN:
                if (resultCode == RESULT_OK) {
                    accountPresenter.checkSignInStatus();
                }
                break;
            case RequestCode.REGISTER:
                if (resultCode == RESULT_OK) {
                    accountPresenter.checkSignInStatus();
                }
                break;
        }
    }

    @OnItemClick({R.id.lvAccountMenu, R.id.lvCommonMenu})
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        switch (parentId) {
            case R.id.lvAccountMenu:
                switch (position) {
                    case 0:
                        startProfileViewActivity();
                        break;
                    case 1:
                        startMyOrderActivity();
                        break;
                    case 2:
                        startMyWishListActivity();
                        break;
                }
                break;
            case R.id.lvCommonMenu:
                switch (position) {
                    case 0:
                        startStoreInformationActivity();
                        break;
                }
                break;
        }
    }


    @Override
    public void viewSignInStatus(boolean isSignedIn, String name) {
        vgLoginRegister.setVisibility(isSignedIn ? View.GONE : View.VISIBLE);
        vgAccountStatus.setVisibility(isSignedIn ? View.VISIBLE : View.GONE);
        vgAccountMenu.setVisibility(isSignedIn ? View.VISIBLE : View.GONE);
        tvAccountStatus.setText(isSignedIn ? getString(R.string.msg_welcome_format, name) : getString(R.string.label_oops));
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RequestCode.LOGIN);
    }

    @Override
    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, RequestCode.REGISTER);
    }

    @Override
    public void startProfileViewActivity() {
        Intent intent = new Intent(this, ProfileDisplayActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMyOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMyWishListActivity() {
        Intent intent = new Intent(this, WishListActivity.class);
        startActivity(intent);
    }

    @Override
    public void startStoreInformationActivity() {
        Intent intent = new Intent(this, StoreInformationActivity.class);
        startActivity(intent);
    }
}

