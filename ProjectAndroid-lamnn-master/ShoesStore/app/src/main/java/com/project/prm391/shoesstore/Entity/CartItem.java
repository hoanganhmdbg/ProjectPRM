package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * Items in the cart.
 */
public class CartItem implements Serializable {
    private Cart cart;
    private Product product;
    private int quantity;

    CartItem(Cart cart, Product product) {
        this(cart, product, 1);
    }

    CartItem(Cart cart, Product product, int quantity) {
        setCart(cart);
        setProduct(product);
        setQuantity(quantity);
    }

    public Cart getCart() {
        return cart;
    }

    private void setCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null.");
        }
        this.cart = cart;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive integer.");
        }
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!cart.equals(cartItem.cart)) return false;
        return product.equals(cartItem.product);
    }

    @Override
    public int hashCode() {
        int result = cart.hashCode();
        result = 31 * result + product.hashCode();
        return result;
    }
}
