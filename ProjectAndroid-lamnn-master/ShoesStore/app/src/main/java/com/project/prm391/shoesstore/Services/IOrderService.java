package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.User;

import java.util.List;


public interface IOrderService {

    Task<List<Order>> getOrdersByUserWithOrderItems(User user);

    Task<Void> createOrder(Order order);

}
