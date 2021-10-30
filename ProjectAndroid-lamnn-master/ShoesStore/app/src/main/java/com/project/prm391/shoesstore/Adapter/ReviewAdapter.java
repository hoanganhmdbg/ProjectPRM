package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lamtu on 2018-03-11.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final Context context;
    private final List<ProductReview> reviews;
    private final int limit;

    public ReviewAdapter(Context context, List<ProductReview> reviews) {
        this(context, reviews, Integer.MAX_VALUE);
    }

    public ReviewAdapter(Context context, List<ProductReview> reviews, int limit) {
        this.context = context;
        this.reviews = reviews;
        this.limit = limit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.review_view_holder_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductReview review = reviews.get(position);
        holder.tvTitle.setText(review.getTitle());
        holder.tvContent.setText(review.getContent());
        holder.tvPostedBy.setText(context.getString(R.string.msg_posted_by_format, review.getUser().getName(), Strings.formatDate(review.getPublishedTime())));
        holder.rbRating.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return Math.max(0, Math.min(reviews.size(), limit));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvPostedBy)
        TextView tvPostedBy;
        @BindView(R.id.rbRating)
        RatingBar rbRating;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
