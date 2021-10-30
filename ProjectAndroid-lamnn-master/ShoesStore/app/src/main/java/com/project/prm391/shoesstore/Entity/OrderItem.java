package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private Order order;
    /**
     * The product.
     */
    private Product product;
    /**
     * The quantity bought by the user.
     */
    private int quantity;
    /**
     * Price at the time the user make the order.
     */
    private double orderPrice;

    OrderItem(Order order, Product product) {
        this(order, product, 1, 0);
    }

    OrderItem(Order order, Product product, int quantity, double orderPrice) {
        setOrder(order);
        setProduct(product);
        setQuantity(quantity);
        setOrderPrice(orderPrice);
    }

    public Order getOrder() {
        return order;
    }

    private void setOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null.");
        }
        this.order = order;
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

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        if (orderPrice < 0) {
            throw new IllegalArgumentException("Order price must not be negative.");
        }
        this.orderPrice = orderPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (!order.equals(orderItem.order)) return false;
        return product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        int result = order.hashCode();
        result = 31 * result + product.hashCode();
        return result;
    }
}
