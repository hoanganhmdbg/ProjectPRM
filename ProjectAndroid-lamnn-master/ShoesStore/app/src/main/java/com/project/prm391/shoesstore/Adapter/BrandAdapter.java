package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lamtu on 2018-02-27.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private final Context context;
    private final List<Brand> brands;
    private Consumer<Brand> onBrandSelectedConsumer;

    public BrandAdapter(Context context, List<Brand> brands) {
        this(context, brands, null);
    }

    public BrandAdapter(Context context, List<Brand> brands, Consumer<Brand> onBrandSelectedConsumer) {
        this.context = context;
        this.brands = brands;
        this.onBrandSelectedConsumer = onBrandSelectedConsumer;
    }

    public void setOnBrandSelectedConsumer(Consumer<Brand> onBrandSelectedConsumer) {
        this.onBrandSelectedConsumer = onBrandSelectedConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.brand_view_holder_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Brand brand = brands.get(position);
        holder.layoutBrand.setOnClickListener(v -> {
            if (onBrandSelectedConsumer != null) {
                onBrandSelectedConsumer.accept(brand);
            }
        });
        holder.tvName.setText(brand.getName());
        holder.pbLoadImage.setVisibility(View.VISIBLE);
        if (Strings.isNullOrEmpty(brand.getImageUrl())) {
            Picasso.with(context).load(R.drawable.image_not_available).into(holder.ivImage);
        } else {
            Picasso.with(context).load(brand.getImageUrl()).into(holder.ivImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.pbLoadImage.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(R.drawable.image_not_available).into(holder.ivImage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.layoutBrand)
        ViewGroup layoutBrand;
        @BindView(R.id.pbLoadImage)
        ProgressBar pbLoadImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
