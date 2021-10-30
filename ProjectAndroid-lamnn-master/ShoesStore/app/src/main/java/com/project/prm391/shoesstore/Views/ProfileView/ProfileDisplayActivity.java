package com.project.prm391.shoesstore.Views.ProfileView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.prm391.shoesstore.Configs.RequestCode;
import com.project.prm391.shoesstore.Presenter.ProfileDisplayPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileDisplayActivity extends AppCompatActivity implements ProfileDisplayView, View.OnClickListener {
    private final ProfileDisplayPresenter profileDisplayPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.btnUpdateProfile)
    Button btnUpdateProfile;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;
    @BindView(R.id.pbLoadImage)
    ProgressBar pbLoadImage;

    public ProfileDisplayActivity() {
        this.profileDisplayPresenter = new ProfileDisplayPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_display_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileDisplayPresenter.loadProfile();
    }

    @OnClick({R.id.btnUpdateProfile, R.id.btnChangePassword})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnUpdateProfile:
                startProfileEditActivity();
                break;
            case R.id.btnChangePassword:
                startChangePasswordActivity();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void displayProfile(String name, String email, String phoneNumber, String address, String photoUrl) {
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhoneNumber.setText(phoneNumber);
        tvAddress.setText(address);
        loadProfileImage(photoUrl);
    }

    private void loadProfileImage(String photoUrl) {
        if (Strings.isNullOrEmpty(photoUrl)) {
            Picasso.with(ProfileDisplayActivity.this).load(R.drawable.ic_user_profile).into(ivPhoto);
        } else {
            pbLoadImage.setVisibility(View.VISIBLE);
            Picasso.with(ProfileDisplayActivity.this).load(photoUrl).into(ivPhoto, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoadImage.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    pbLoadImage.setVisibility(View.GONE);
                    Picasso.with(ProfileDisplayActivity.this).load(R.drawable.ic_user_profile).into(ivPhoto);
                }
            });
        }
    }

    @Override
    public void startProfileEditActivity() {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivityForResult(intent, RequestCode.EDIT_PROFILE);
    }

    @Override
    public void startChangePasswordActivity() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivityForResult(intent, RequestCode.CHANGE_PASSWORD);
    }
}
