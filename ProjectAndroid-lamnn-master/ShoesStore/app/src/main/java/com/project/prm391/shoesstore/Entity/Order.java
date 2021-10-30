package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Orders made by users.
 */
public class Order implements Serializable {
    /**
     * Set of order items.
     */
    private final Set<OrderItem> orderItems = new HashSet<>();
    /**
     * Order ID.
     */
    private String id;
    /**
     * The user that placed the order.
     */
    private User user;
    /**
     * The time that the order is created.
     */
    private Date timestamp = new Date();
    /**
     * Delivery information of the order.
     */
    private OrderDeliveryInfo deliveryInfo = new OrderDeliveryInfo();
    /**
     * The payment method.
     */
    private PaymentMethod paymentMethod = PaymentMethod.CASH_ON_DELIVERY;
    /**
     * Status of the order.
     */
    private OrderStatus status = new OrderStatus();
    /**
     * Message embedded in the order.
     */
    private String message = "";

    public Order() {
    }

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public OrderDeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(OrderDeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get order item of a product.
     *
     * @return The order item of the product, or null if the product does not exist in the order.
     */
    public OrderItem getOrderItem(Product product) {
        return orderItems.stream().filter(orderItem -> orderItem.getProduct().equals(product)).findFirst().orElse(null);
    }

    /**
     * Get all the order items.
     *
     * @return A read-only set of order items.
     */
    public Set<OrderItem> getOrderItems() {
        return Collections.unmodifiableSet(orderItems);
    }

    /**
     * Get the set of products in the order.
     *
     * @return The {@link Product} set.
     */
    public Set<Product> getProducts() {
        return orderItems.stream().map(orderItem -> orderItem.getProduct()).collect(Collectors.toSet());
    }

    /**
     * Check if product exists in the order.
     *
     * @param product The {@link Product} object.
     * @return True if it exists, otherwise False.
     */
    public boolean hasProduct(Product product) {
        return orderItems.stream().anyMatch(orderItem -> orderItem.getProduct().equals(product));
    }

    /**
     * Add new product to the order items. If the product already exists, nothing will happen,
     * and the existing order item will be returned.
     *
     * @param product    The {@link Product} object.
     * @param quantity   The quantity of product.
     * @param orderPrice The order price of product.
     * @return The newly added or existing order item.
     */
    public OrderItem addOrderItem(Product product, int quantity, double orderPrice) {
        if (hasProduct(product)) {
            return getOrderItem(product);
        }
        OrderItem orderItem = new OrderItem(this, product, quantity, orderPrice);
        orderItems.add(orderItem);
        return orderItem;
    }

    /**
     * Remove an existing product in the order.
     * If the product does not exist, nothing will happen.
     *
     * @param product The {@link Product} object.
     * @return True if it previously exists in the order, otherwise False.
     */
    public boolean removeOrderItem(Product product) {
        return orderItems.removeIf(orderItem -> orderItem.getProduct().equals(product));
    }

    /**
     * Clear the order items.
     */
    public void clearOrderItems() {
        orderItems.clear();
    }

    /**
     * Get total price of the order.
     *
     * @return The total price of the order.
     */
    public double getTotalPrice() {
        return getOrderItems()
                .stream()
                .collect(Collectors.summingDouble(orderItem -> orderItem.getQuantity() * orderItem.getOrderPrice()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id != null ? id.equals(order.id) : order.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
