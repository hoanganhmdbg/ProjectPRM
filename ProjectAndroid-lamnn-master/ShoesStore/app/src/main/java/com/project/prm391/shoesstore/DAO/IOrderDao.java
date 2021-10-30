package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.User;

import java.util.List;

/**
 * Notes:
 * <ul>
 * <li>
 * The {@link Order} object returned by methods in this interface will not have the order items fetched.
 * They must be fetched manually (e.g: with the {@link IOrderDao#fetchOrderItems(Order)} method).<br/>
 * </li>
 * <li>
 * The {@link com.project.prm391.shoesstore.Entity.Product} object in each order item is also not fetched.
 * They must be fetched manually (e.g: with the {@link IProductDao#fetchProduct(Product)} method).
 * </li>
 * </ul>
 */
public interface IOrderDao {
    /**
     * Get the list of orders.
     *
     * @return A task that returns the list of orders.
     */
    Task<List<Order>> getAllOrders();

    /**
     * Get order by ID.
     *
     * @param id ID of the order.
     * @return A task that returns the order, or null if ID does not exist.
     */
    Task<Order> getOrderById(String id);

    /**
     * Get the list of orders made by user.
     *
     * @param user The user.
     * @return A task that returns the list of orders.
     */
    Task<List<Order>> getOrdersByUser(User user);

    /**
     * Fetch information to the {@link Order} object.<br/>
     * The ID of the object must be already set.<br/>
     *
     * @param order The {@link Order} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchOrder(Order order);

    /**
     * Create a new order.<br/>
     * Note that the ID of the new order will be set to the input {@link Order} parameter.
     *
     * @param order The {@link Order} object.
     * @return A task that returns nothing.
     */
    Task<Void> createOrder(Order order);

    /**
     * Fetch order items to the {@link Order} object.<br/>
     * The ID of the object must be already set.<br/>
     *
     * @param order The {@link Order} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchOrderItems(Order order);


}
