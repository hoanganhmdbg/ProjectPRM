package com.project.prm391.shoesstore.Views.ProductDetailView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lamtu on 2018-03-04.
 */

public class ProductImageFragment extends Fragment {
    @BindView(R.id.ivProductImage)
    ImageView ivProductImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_product_image_layout, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String imageUrl = bundle.getString("imageUrl");
        if (Strings.isNullOrEmpty(imageUrl)) {
            Picasso.with(getContext()).load(R.drawable.image_not_available).into(ivProductImage);
        } else {
            Picasso.with(getContext()).load(imageUrl).into(ivProductImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Picasso.with(getContext()).load(R.drawable.image_not_available).into(ivProductImage);
                }
            });
        }
        return view;
    }
}
