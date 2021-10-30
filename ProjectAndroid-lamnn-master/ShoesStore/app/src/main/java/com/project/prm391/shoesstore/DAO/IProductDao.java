package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;

import java.util.List;

/**
 * Notes:
 * <ul>
 * <li>The {@link Product} object returned by methods in this interface will not have
 * {@link Product#category}, {@link Product#brand}, {@link Product#gender} fields and the reviews fetched.
 * They must be fetched manually or be set from cache.
 * </li>
 * </ul>
 */
public interface IProductDao {
    /**
     * Get the list of products.
     *
     * @return A task that returns the list of products.
     */
    Task<List<Product>> getAllProducts();

    /**
     * Get product by ID.
     *
     * @param id ID of the product.
     * @return A task that returns the product, or null if ID does not exist.
     */
    Task<Product> getProductById(String id);

    /**
     * Fetch information to the {@link Product} object.<br/>
     * The ID of the object must be already set.<br/>
     *
     * @param product The {@link Product} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchProduct(Product product);

    /**
     * Create a product.<br/>
     * Note that the ID of the new product will be set to the input {@link Product} parameter.
     *
     * @param product The {@link Product} object.
     * @return A task that returns nothing.
     */
    Task<Void> createProduct(Product product);

    /**
     * Fetch reviews to the {@link Product} object.<br/>
     * The ID of the object must be already set.<br/>
     *
     * @param product The {@link Product} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchProductReviews(Product product);

    /**
     * Create a new review if the user has not made review, or update the existing review of the user.
     *
     * @param review The {@link ProductReview} object.
     * @return A task that returns nothing.
     */
    Task<Void> createOrUpdateProductReview(ProductReview review);

    /**
     * Get the list of favorite products of a specified user.
     *
     * @param user The {@link User} object.
     * @return A task that returns the list of products.
     */
    Task<List<Product>> getUserWishList(User user);

    /**
     * Check if the product is in user wish list.
     *
     * @param user    The {@link User} object.
     * @param product The {@link Product} object.
     * @return A task that returns a boolean indicating whether the product
     * is in user wish list.
     */
    Task<Boolean> checkIfProductInUserWishList(User user, Product product);

    /**
     * Add product to user wish list.
     *
     * @param user    The {@link User} object.
     * @param product The {@link Product} object.
     * @return A task that returns True if the product was added to user wish list,
     * or False if product already in user wish list.
     */
    Task<Boolean> addProductToUserWishList(User user, Product product);

    /**
     * Remove product from user wish list.
     *
     * @param user    The {@link User} object.
     * @param product The {@link Product} object.
     * @return A task that returns True if the product was removed from user wish list,
     * or False if product is not in user wish list.
     */
    Task<Boolean> removeProductFromUserWishList(User user, Product product);
}
