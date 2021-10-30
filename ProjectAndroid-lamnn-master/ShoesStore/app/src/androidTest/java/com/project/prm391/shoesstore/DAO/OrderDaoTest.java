package com.project.prm391.shoesstore.DAO;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.DeliveryStatus;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.OrderItem;
import com.project.prm391.shoesstore.Entity.PaymentMethod;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.User;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by nguyen on 3/18/2018.
 */

public class OrderDaoTest {
    private Context context;
    private FirebaseApp firebaseApp;
    private OrderDao orderDao;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        firebaseApp = FirebaseApp.initializeApp(context);
        orderDao = new OrderDao(FirebaseFirestore.getInstance(firebaseApp));
    }

    @Test
    public void getOrderByIdTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2018, 2, 15, 9, 0, 0);
        Order order = Tasks.await(orderDao.getOrderById("Nb0oQ4VxpHpNlUZJhtKF"));
        assertEquals("Nb0oQ4VxpHpNlUZJhtKF", order.getId());
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", order.getUser().getId());
        assertEquals(calendar.getTime(), order.getTimestamp());
        assertEquals("Lê Cao Nguyên", order.getDeliveryInfo().getName());
        assertEquals("nguyenlc1993@gmail.com", order.getDeliveryInfo().getEmail());
        assertEquals("01296685555", order.getDeliveryInfo().getPhoneNumber());
        assertEquals("Thôn 9, Thạch Hòa, Thạch Thất, Hà Nội", order.getDeliveryInfo().getAddress());
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, order.getPaymentMethod());
        assertEquals(true, order.getStatus().isAccepted());
        assertEquals(true, order.getStatus().isPaid());
        assertEquals(DeliveryStatus.DELIVERED, order.getStatus().getDeliveryStatus());
        assertEquals("", order.getMessage());
    }

    @Test
    public void getOrdersByUserTest() throws ExecutionException, InterruptedException {
        User user = new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1");
        List<Order> orders = Tasks.await(orderDao.getOrdersByUser(user));
        assertEquals(2, orders.size());
    }

    @Test
    public void fetchOrderTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2018, 2, 15, 9, 0, 0);
        Order order = new Order("Nb0oQ4VxpHpNlUZJhtKF");
        boolean success = Tasks.await(orderDao.fetchOrder(order));
        assertTrue(success);
        assertEquals("Nb0oQ4VxpHpNlUZJhtKF", order.getId());
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", order.getUser().getId());
        assertEquals(calendar.getTime(), order.getTimestamp());
        assertEquals("Lê Cao Nguyên", order.getDeliveryInfo().getName());
        assertEquals("nguyenlc1993@gmail.com", order.getDeliveryInfo().getEmail());
        assertEquals("01296685555", order.getDeliveryInfo().getPhoneNumber());
        assertEquals("Thôn 9, Thạch Hòa, Thạch Thất, Hà Nội", order.getDeliveryInfo().getAddress());
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, order.getPaymentMethod());
        assertEquals(true, order.getStatus().isAccepted());
        assertEquals(true, order.getStatus().isPaid());
        assertEquals(DeliveryStatus.DELIVERED, order.getStatus().getDeliveryStatus());
        assertEquals("", order.getMessage());
    }

    @Test
    public void fetchOrderItemsTest() throws ExecutionException, InterruptedException {
        Order order = new Order("Nb0oQ4VxpHpNlUZJhtKF");
        boolean success = Tasks.await(orderDao.fetchOrderItems(order));
        assertTrue(success);
        assertEquals(1, order.getOrderItems().size());
        OrderItem orderItem = order.getOrderItem(new Product("p5f1ITDLBBvobOOixeHR"));
        assertNotNull(orderItem);
        assertEquals(1, orderItem.getQuantity());
        assertEquals(90, orderItem.getOrderPrice(), 0.0001);
    }

    @Test
    @Ignore
    public void createOrderTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2018, 2, 16, 11, 30, 0);
        Order order = new Order();
        order.setUser(new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        order.setTimestamp(calendar.getTime());
        order.getDeliveryInfo().setName("Lê Chí Linh");
        order.getDeliveryInfo().setEmail("linhhaaco@gmail.com");
        order.getDeliveryInfo().setPhoneNumber("0912036538");
        order.getDeliveryInfo().setAddress("294 Nguyễn Văn Cừ, Hạ Long, Quảng Ninh");
        order.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        order.getStatus().setAccepted(true);
        order.getStatus().setPaid(true);
        order.getStatus().setDeliveryStatus(DeliveryStatus.DELIVERING);
        order.setMessage("Nếu không liên lạc được với số trên thì gọi số 01296685555.");
        order.addOrderItem(new Product("6cZ67RXbmuKyOIJqlmkv"), 1, 150);
        order.addOrderItem(new Product("woyGOWk5ciklf47bV3cf"), 2, 95);

        Tasks.await(orderDao.createOrder(order));
        assertNotNull(order.getId());
    }
}
