package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.DAO.IUserDao;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;

import java.util.List;
import java.util.Set;

/**
 * Created by nguyen on 3/25/2018.
 */

public class ReviewService implements IReviewService {
    private final IProductDao productDao;
    private final IUserDao userDao;

    public ReviewService(IProductDao productDao, IUserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Set<ProductReview>> fetchProductReviews(Product product) {
        return productDao.fetchProductReviews(product).continueWithTask(task -> {
            if (task.getResult()) {
                return userDao.getAllUsers().continueWith(task1 -> {
                    List<User> users = task1.getResult();
                    Set<ProductReview> reviews = product.getProductReviews();
                    reviews.forEach(review -> mergeProductReviewFields(review, users));
                    return reviews;
                });
            } else {
                return Tasks.forResult(product.getProductReviews());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Void> createOrUpdateProductReview(ProductReview review) {
        return productDao.createOrUpdateProductReview(review);
    }

    private ProductReview mergeProductReviewFields(ProductReview review, List<User> users) {
        users.stream()
                .filter(user -> user.equals(review.getUser()))
                .findFirst()
                .ifPresent(user -> review.setUser(user));
        return review;
    }
}
