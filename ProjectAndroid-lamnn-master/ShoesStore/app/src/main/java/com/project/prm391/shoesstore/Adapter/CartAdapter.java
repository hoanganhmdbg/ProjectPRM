package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Utils.Strings;
import com.project.prm391.shoesstore.Views.CartView.CartActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duytq on 3/12/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = CartAdapter.class.getName();
    private final Context context;
    private List<Product> products;
    private IDataCacheService dataCacheService;

    public CartAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        dataCacheService = ServiceManager.getInstance().getDataCacheService();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_item_recycle_view_cart_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvCurrentPrice.setText(Strings.formatPrice(product.getCurrentPrice()));
        holder.tvQuantity.setText(Integer.toString(dataCacheService.getCart().getCartItem(product).getQuantity()));
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

        /**
         * Delete icon click event
         */
        holder.ivRemoveProduct.setOnClickListener(view -> {
            products.remove(position);
            dataCacheService.getCart().removeCartItem(product);
            context.startActivity(new Intent(context, CartActivity.class));
            notifyDataSetChanged();
        });

        holder.btnDecreaseQuantity.setOnClickListener(view -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity = quantity - 1;
                holder.tvQuantity.setText(Integer.toString(quantity));
                dataCacheService.getCart().addCartItem(product).setQuantity(quantity);
                //Reload CartActivity
                context.startActivity(new Intent(context, CartActivity.class));
            }
        });

        holder.btnIncreaseQuantity.setOnClickListener(view -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (quantity < Constants.CART_ITEM_MAX_QUANTITY) {
                quantity = quantity + 1;
                holder.tvQuantity.setText(String.valueOf(quantity));
                dataCacheService.getCart().addCartItem(product).setQuantity(quantity);
                //Reload CartActivity
                context.startActivity(new Intent(context, CartActivity.class));
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
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        @BindView(R.id.ivRemoveProduct)
        ImageView ivRemoveProduct;
        @BindView(R.id.btnDecreaseQuantity)
        ImageButton btnDecreaseQuantity;
        @BindView(R.id.btnIncreaseQuantity)
        ImageButton btnIncreaseQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
