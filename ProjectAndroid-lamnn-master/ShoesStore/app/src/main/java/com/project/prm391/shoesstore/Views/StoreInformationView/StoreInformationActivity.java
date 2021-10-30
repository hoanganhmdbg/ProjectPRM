package com.project.prm391.shoesstore.Views.StoreInformationView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.prm391.shoesstore.Entity.StoreInformation;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duytq on 3/24/2018.
 */

public class StoreInformationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final StoreInformation storeInformation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvWebsiteUrl)
    TextView tvWebsiteUrl;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    public StoreInformationActivity() {
        storeInformation = ServiceManager.getInstance().getDataCacheService().getStoreInformation();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_information_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadStoreInformation();
        initializeMap();
    }

    private void loadStoreInformation() {
        tvName.setText(storeInformation.getName());
        tvEmail.setText(storeInformation.getEmail());
        tvPhoneNumber.setText(storeInformation.getPhoneNumber());
        tvWebsiteUrl.setText(storeInformation.getWebsiteUrl());
        tvAddress.setText(storeInformation.getAddress());
    }

    private void initializeMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng storeLocation = new LatLng(storeInformation.getLocationLatitude(), storeInformation.getLocationLongitude());
        googleMap.addMarker(new MarkerOptions().position(storeLocation).title(storeInformation.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 1000, null);
    }
}
