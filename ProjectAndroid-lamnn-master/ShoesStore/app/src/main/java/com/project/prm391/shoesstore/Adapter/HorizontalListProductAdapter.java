package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lamtu on 2018-03-23.
 */

public class HorizontalListProductAdapter extends RecyclerView.Adapter<HorizontalListProductAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> products;
    private Consumer<Product> onProductSelectedConsumer;

    public HorizontalListProductAdapter(Context context, List<Product> products) {
        this(context, products, null);
    }

    public HorizontalListProductAdapter(Context context, List<Product> products, Consumer<Product> onProductSelectedConsumer) {
        this.context = context;
        this.products = products;
        this.onProductSelectedConsumer = onProductSelectedConsumer;
    }

    public void setOnProductSelectedConsumer(Consumer<Product> onProductSelectedConsumer) {
        this.onProductSelectedConsumer = onProductSelectedConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.horizontal_list_product_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        List<String> imageUrls = product.getImageUrls();
        if (imageUrls.isEmpty()) {
            Picasso.with(context).load(R.drawable.image_not_available).into(holder.ivImage);
        } else {
            Picasso.with(context).load(imageUrls.get(0)).into(holder.ivImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(R.drawable.image_not_available).into(holder.ivImage);
                }
            });
        }
        holder.tvName.setText(product.getName());
        if (product.isAvailable()) {
            holder.layoutNotAvailable.setVisibility(View.INVISIBLE);
            holder.layoutAvailable.setVisibility(View.VISIBLE);
            holder.tvCurrentPrice.setText(Strings.formatPrice(product.getCurrentPrice()));
            holder.tvOriginalPrice.setText(Strings.formatPrice(product.getOriginalPrice()));
            holder.tvDiscountRate.setText(Strings.formatDiscountRate(product.getDiscountRate()));

            if (product.getDiscountRate() == 0) {
                holder.layoutDiscount.setVisibility(View.INVISIBLE);
            } else {
                holder.layoutDiscount.setVisibility(View.VISIBLE);
                if (product.getDiscountRate() == 1) {
                    holder.tvDiscountRate.setText(context.getString(R.string.label_free));
                }
            }
        } else {
            holder.layoutNotAvailable.setVisibility(View.VISIBLE);
            holder.layoutAvailable.setVisibility(View.INVISIBLE);
        }

        Double averageRatings = product.getAverageRatings();
        holder.rbAverageRatings.setRating(averageRatings == null ? 0.0f : averageRatings.floatValue());

        holder.layoutProduct.setOnClickListener(v -> {
            if (onProductSelectedConsumer != null) {
                onProductSelectedConsumer.accept(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCurrentPrice)
        TextView tvCurrentPrice;
        @BindView(R.id.tvOriginalPrice)
        TextView tvOriginalPrice;
        @BindView(R.id.tvDiscountRate)
        TextView tvDiscountRate;
        @BindView(R.id.rbAverageRatings)
        RatingBar rbAverageRatings;
        @BindView(R.id.layoutProduct)
        ViewGroup layoutProduct;
        @BindView(R.id.layoutAvailable)
        ViewGroup layoutAvailable;
        @BindView(R.id.layoutNotAvailable)
        ViewGroup layoutNotAvailable;
        @BindView(R.id.layoutDiscount)
        ViewGroup layoutDiscount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

