package com.project.prm391.shoesstore.Views.ProductDetailView;

import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;

import java.util.List;

/**
 * Created by lamtu on 2018-03-10.
 */

public interface ProductDetailView {

    void displayProduct(Product product);

    void displayProductReviews(List<ProductReview> reviews, int limit);

    void displayAverageRatings(Double averageRatings);

    void notifyAddToCartSuccess();

    void notifyAddToCartFailed(String error);

    void notifyException(Exception ex);

    void toggleLoadReviewsProgressBar(boolean show);

    void toggleReviewButtonLabel(boolean hasReviewed);

    void toggleReviewButtonEnabled(boolean enabled);

    void toggleAddRemoveFavoriteButton(Boolean action);

    void toggleAddRemoveFavoriteProgressBar(boolean show);

    void startDoReviewActivity(ProductReview review);

    void notifyReviewRequireSignIn();

    void notifyFavoriteActionRequireSignIn();

    void notifyFavoriteActionSuccessful(String message);

    void finishActivity();
}
