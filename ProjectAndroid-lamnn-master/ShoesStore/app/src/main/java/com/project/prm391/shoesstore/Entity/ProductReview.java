package com.project.prm391.shoesstore.Entity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Review of a product.
 */
public class ProductReview implements Serializable, Comparable<ProductReview> {
    /**
     * The product of the review.
     */
    private Product product;
    /**
     * The user that published the review.
     */
    private User user;
    /**
     * Review title.
     */
    private String title;
    /**
     * Review content.
     */
    private String content;
    /**
     * Time that the review was published.
     */
    private Date publishedTime = new Date();
    /**
     * Product rating in the review.
     */
    private int rating;

    ProductReview(Product product, User user) {
        this(product, user, 3);
    }

    ProductReview(Product product, User user, int rating) {
        setProduct(product);
        setUser(user);
        setRating(rating);
    }

    public Product getProduct() {
        return product;
    }

    private void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
        this.product.updateAverageRatings();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductReview that = (ProductReview) o;

        if (!product.equals(that.product)) return false;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull ProductReview productReview) {
        Date publishedTime1 = this.getPublishedTime();
        Date publishedTime2 = productReview.getPublishedTime();
        if (publishedTime1 == null) {
            return -1;
        } else if (publishedTime2 == null) {
            return 1;
        } else {
            return -publishedTime1.compareTo(publishedTime2);
        }
    }
}
