package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;

import java.util.Set;

/**
 * Created by nguyen on 3/25/2018.
 */

public interface IReviewService {
    /**
     * Fetch product reviews.<br/>
     * This method will also fetch and merge the {@link ProductReview#user} field.
     *
     * @param product The {@link Product} object.
     * @return A task that returns the set of fetched reviews.
     */
    Task<Set<ProductReview>> fetchProductReviews(Product product);

    /**
     * Create or update product review.
     *
     * @param review The {@link ProductReview} object.
     * @return A task that returns nothing.
     */
    Task<Void> createOrUpdateProductReview(ProductReview review);
}
