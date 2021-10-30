package com.project.prm391.shoesstore.Services;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.project.prm391.shoesstore.DAO.IOrderDao;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {

    private final IOrderDao orderDao;
    private final IProductDao productDao;

    public OrderService(IOrderDao orderDao, IProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Override
    public Task<List<Order>> getOrdersByUserWithOrderItems(User user) {
        return orderDao.getOrdersByUser(user).continueWithTask(task -> {
            List<Order> orders = task.getResult();
            orders.sort(Comparator.comparing(Order::getTimestamp).reversed());
            List<Task<Boolean>> taskList = orders.stream().map(order -> orderDao.fetchOrderItems(order)).collect(Collectors.toList());
            return Tasks.whenAll(taskList).continueWith(aggregateTask -> {
                aggregateTask.getResult();
                return orders;
            });
        });
    }

    @Override
    public Task<Void> createOrder(Order order) {
        return orderDao.createOrder(order);
    }
}
