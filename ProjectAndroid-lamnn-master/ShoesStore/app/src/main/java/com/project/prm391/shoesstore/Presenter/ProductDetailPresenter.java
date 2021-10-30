package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Entity.Cart;
import com.project.prm391.shoesstore.Entity.CartItem;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Exceptions.AuthenticationException;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.IReviewService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Services.IWishListService;
import com.project.prm391.shoesstore.Views.ProductDetailView.ProductDetailView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by lamtu on 2018-03-10.
 */

public class ProductDetailPresenter {
    private static final String TAG = ProductDetailPresenter.class.getName();

    private final ProductDetailView view;
    private final IDataCacheService dataCacheService;
    private final IUserService userService;
    private final IReviewService reviewService;
    private final IWishListService wishListService;
    private final Resources resources;

    private Product product;
    private Boolean addRemoveWishListAction = null;

    public ProductDetailPresenter(ProductDetailView view) {
        this.view = view;
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
        this.userService = ServiceManager.getInstance().getUserService();
        this.reviewService = ServiceManager.getInstance().getReviewService();
        this.wishListService = ServiceManager.getInstance().getWishListService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    public Product getProduct() {
        return product;
    }

    public void setAddRemoveWishListAction(Boolean addRemoveWishListAction) {
        this.addRemoveWishListAction = addRemoveWishListAction;
        view.toggleAddRemoveFavoriteButton(addRemoveWishListAction);
    }

    public void loadProduct(Product product) {
        this.product = product;
        displayProduct(product);
    }

    private void displayProduct(Product product) {
        if (product != null) {
            view.displayProduct(product);
            loadReviews(Constants.REVIEW_LIST_LIMIT);
        } else {
            view.finishActivity();
        }
    }

    public void loadReviews() {
        loadReviews(Integer.MAX_VALUE);
    }

    public void loadReviews(int limit) {
        if (product == null) {
            return;
        }
        view.displayProductReviews(new ArrayList<>(), 0);
        view.toggleLoadReviewsProgressBar(true);
        view.toggleReviewButtonEnabled(false);
        reviewService.fetchProductReviews(product).addOnCompleteListener(task -> {
            view.toggleLoadReviewsProgressBar(false);
            view.toggleReviewButtonEnabled(true);
            if (task.isSuccessful()) {
                Set<ProductReview> productReviews = task.getResult();
                view.displayProductReviews(new ArrayList<>(productReviews), limit);
                view.displayAverageRatings(product.getAverageRatings());
                checkIfUserHasReviewed();
            } else {
                Exception e = task.getException();
                Log.e(TAG, null, e);
                view.notifyException(e);
            }
        });
    }

    public void addToCart() {
        if (product == null) {
            view.notifyAddToCartFailed(resources.getString(R.string.label_oops));
            return;
        }
        if (product.isAvailable()) {
            Cart cart = dataCacheService.getCart();
            if (cart.hasProduct(product)) {
                CartItem cartItem = cart.getCartItem(product);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else {
                cart.addCartItem(product);
            }
            view.notifyAddToCartSuccess();
        } else {
            view.notifyAddToCartFailed(resources.getString(R.string.msg_product_not_available));
        }
    }

    public void checkIfUserHasReviewed() {
        if (userService.isSignedIn()) {
            User user = userService.getCurrentUserProfile();
            if (user != null) {
                view.toggleReviewButtonLabel(product.hasUserReviewed(user));
            } else {
                view.toggleLoadReviewsProgressBar(false);
            }
        } else {
            view.toggleLoadReviewsProgressBar(false);
        }
    }

    public void doReview() {
        User user = userService.getCurrentUserProfile();
        if (!userService.isSignedIn() || user == null) {
            view.notifyReviewRequireSignIn();
            return;
        } else {
            ProductReview review = product.hasUserReviewed(user) ? product.getProductReview(user) : product.addProductReview(user);
            view.startDoReviewActivity(review);
        }
    }

    public void checkIfProductInWishList() {
        if (!userService.isSignedIn()) {
            setAddRemoveWishListAction(null);
        } else {
            view.toggleAddRemoveFavoriteProgressBar(true);
            wishListService.checkIfProductInCurrentUserWishList(product)
                    .addOnCompleteListener(task -> {
                        view.toggleAddRemoveFavoriteProgressBar(false);
                        if (task.isSuccessful()) {
                            boolean result = task.getResult();
                            setAddRemoveWishListAction(!result);
                        } else {
                            Exception e = task.getException();
                            if (e instanceof AuthenticationException) {
                                setAddRemoveWishListAction(null);
                            } else {
                                Log.e(TAG, null, e);
                                view.notifyException(e);
                                setAddRemoveWishListAction(null);
                            }
                        }
                    });
        }
    }

    public void addRemoveFavorite() {
        if (!userService.isSignedIn() || addRemoveWishListAction == null) {
            setAddRemoveWishListAction(null);
            view.notifyFavoriteActionRequireSignIn();
        } else {
            view.toggleAddRemoveFavoriteProgressBar(true);
            if (addRemoveWishListAction) {
                wishListService.addProductToCurrentUserWishList(product)
                        .addOnCompleteListener(task -> {
                            view.toggleAddRemoveFavoriteProgressBar(false);
                            if (task.isSuccessful()) {
                                boolean result = task.getResult();
                                if (result) {
                                    view.notifyFavoriteActionSuccessful(resources.getString(R.string.msg_product_add_to_wish_list_success));
                                } else {
                                    view.notifyFavoriteActionSuccessful(resources.getString(R.string.msg_product_already_in_wish_list));
                                }
                                setAddRemoveWishListAction(false);
                            } else {
                                Exception e = task.getException();
                                if (e instanceof AuthenticationException) {
                                    setAddRemoveWishListAction(null);
                                    view.notifyFavoriteActionRequireSignIn();
                                } else {
                                    Log.e(TAG, null, e);
                                    view.notifyException(e);
                                }
                            }
                        });
            } else {
                wishListService.removeProductFromUserWishList(product)
                        .addOnCompleteListener(task -> {
                            view.toggleAddRemoveFavoriteProgressBar(false);
                            if (task.isSuccessful()) {
                                boolean result = task.getResult();
                                if (result) {
                                    view.notifyFavoriteActionSuccessful(resources.getString(R.string.msg_product_remove_from_wish_list_success));
                                } else {
                                    view.notifyFavoriteActionSuccessful(resources.getString(R.string.msg_product_not_in_wish_list));
                                }
                                setAddRemoveWishListAction(true);
                            } else {
                                Exception e = task.getException();
                                if (e instanceof AuthenticationException) {
                                    setAddRemoveWishListAction(null);
                                    view.notifyFavoriteActionRequireSignIn();
                                } else {
                                    Log.e(TAG, null, e);
                                    view.notifyException(e);
                                }
                            }
                        });
            }
        }
    }

}
