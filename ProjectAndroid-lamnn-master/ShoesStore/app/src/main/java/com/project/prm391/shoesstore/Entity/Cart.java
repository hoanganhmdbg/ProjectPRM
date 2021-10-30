package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Cart containing the products that the user is going to buy.
 */
public class Cart implements Serializable {
    /**
     * Items in the cart
     */
    private final Set<CartItem> cartItems = new HashSet<>();
    /**
     * The user of the cart.
     */
    private User user;

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get cart item of a product
     *
     * @return The cart item of the product, or null if the product does not exist in the cart.
     */
    public CartItem getCartItem(Product product) {
        return cartItems.stream().filter(cartItem -> cartItem.getProduct().equals(product)).findFirst().orElse(null);
    }

    /**
     * Get all the cart items.
     *
     * @return A read-only set of cart items.
     */
    public Set<CartItem> getCartItems() {
        return Collections.unmodifiableSet(cartItems);
    }

    /**
     * Get the set of products in the cart.
     *
     * @return The {@link Product} set.
     */
    public Set<Product> getProducts() {
        return cartItems.stream().map(cartItem -> cartItem.getProduct()).collect(Collectors.toSet());
    }

    /**
     * Check if product exists in the cart.
     *
     * @param product The {@link Product} object.
     * @return True if it exists, otherwise False.
     */
    public boolean hasProduct(Product product) {
        return cartItems.stream().anyMatch(cartItem -> cartItem.getProduct().equals(product));
    }

    /**
     * Add new product to the cart, and set quantity equals to 1. If the product already exists, nothing will happen,
     * and the existing cart item will be returned.
     *
     * @param product The {@link Product} object.
     * @return @return The newly added or existing cart item.
     */
    public CartItem addCartItem(Product product) {

        return addCartItem(product, 1);
    }

    /**
     * Add new product to the cart with specified quantity. If the product already exists, nothing will happen,
     * and the existing cart item will be returned.
     *
     * @param product  The {@link Product} object.
     * @param quantity The quantity of product.
     * @return The newly added or existing cart item.
     */
    public CartItem addCartItem(Product product, int quantity) {
        if (hasProduct(product)) {
            return getCartItem(product);
        }
        CartItem cartItem = new CartItem(this, product, quantity);
        cartItems.add(cartItem);
        return cartItem;
    }

    /**
     * Remove an existing product in the cart.
     * If the product does not exist, nothing will happen.
     *
     * @param product The {@link Product} object.
     * @return True if it previously exists in the cart, otherwise False.
     */
    public boolean removeCartItem(Product product) {
        return cartItems.removeIf(cartItem -> cartItem.getProduct().equals(product));
    }

    /**
     * Clear the cart.
     */
    public void clearCartItems() {
        cartItems.clear();
    }

    /**
     * Gets the total price of the cart.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        return getCartItems()
                .stream()
                .collect(Collectors.summingDouble(cartItem -> cartItem.getQuantity()
                        * cartItem.getProduct().getCurrentPrice()
                ));

    }

}
