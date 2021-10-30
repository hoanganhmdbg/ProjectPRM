package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.project.prm391.shoesstore.Entity.DeliveryStatus;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.OrderDeliveryInfo;
import com.project.prm391.shoesstore.Entity.OrderItem;
import com.project.prm391.shoesstore.Entity.OrderStatus;
import com.project.prm391.shoesstore.Entity.PaymentMethod;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDao extends BaseDao implements IOrderDao {
    public OrderDao() {
        super();
    }

    public OrderDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public Task<List<Order>> getAllOrders() {
        return firestore
                .collection("orders")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractOrder(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    @Override
    public Task<Order> getOrderById(String id) {
        return firestore
                .collection("orders")
                .document(id)
                .get()
                .continueWith(task -> extractOrder(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<List<Order>> getOrdersByUser(User user) {
        DocumentReference userRef = firestore.collection("users").document(user.getId());
        return firestore
                .collection("orders")
                .whereEqualTo("user", userRef)
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractOrder(snapshot, null))
                        .peek(order -> order.setUser(user))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    @Override
    public Task<Boolean> fetchOrder(Order order) {
        return firestore
                .collection("orders")
                .document(order.getId())
                .get()
                .continueWith(task -> extractOrder(task.getResult(), order) != null)
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Void> createOrder(Order order) {
        Map<String, Object> map = convertOrderToMap(order);
        return firestore
                .collection("orders")
                .add(map)
                .continueWith(task -> {
                    String id = task.getResult().getId();
                    order.setId(id);
                    return order;
                })
                .continueWithTask(task -> createOrderItems(task.getResult()))
                .continueWith(wrapDaoException());
    }

    private Task<Void> createOrderItems(Order order) {
        CollectionReference collectionRef = firestore.collection("orders").document(order.getId()).collection("items");
        WriteBatch batch = firestore.batch();
        order.getOrderItems().forEach(item -> batch.set(collectionRef.document(item.getProduct().getId()), convertOrderItemToMap(item)));
        return batch.commit();
    }

    @Override
    public Task<Boolean> fetchOrderItems(Order order) {
        return firestore
                .collection("orders")
                .document(order.getId())
                .get()
                .continueWithTask(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        return Tasks.forResult(false);
                    }
                    return documentSnapshot.getReference()
                            .collection("items")
                            .get()
                            .continueWith(task1 -> {
                                QuerySnapshot querySnapshot = task1.getResult();
                                order.clearOrderItems();
                                querySnapshot.forEach(snapshot -> extractAndAddOrderItem(order, snapshot));
                                return true;
                            });
                }).continueWith(wrapDaoException());
    }

    private Order extractOrder(DocumentSnapshot snapshot, Order order) {
        if (!snapshot.exists()) {
            return null;
        }
        if (order == null) {
            order = new Order();
        }
        order.setId(snapshot.getId());
        order.setUser(new User(snapshot.getDocumentReference("user").getId()));
        order.setTimestamp(snapshot.getDate("timestamp"));
        order.setDeliveryInfo(extractOrderDeliveryInfo((Map<String, Object>) snapshot.get("deliveryInfo"), order.getDeliveryInfo()));
        order.setPaymentMethod(PaymentMethod.valueOf(snapshot.getString("paymentMethod")));
        order.setStatus(extractOrderStatus((Map<String, Object>) snapshot.get("status"), order.getStatus()));
        order.setMessage(snapshot.getString("message"));
        return order;
    }

    private OrderDeliveryInfo extractOrderDeliveryInfo(Map<String, Object> map, OrderDeliveryInfo deliveryInfo) {
        if (deliveryInfo == null) {
            deliveryInfo = new OrderDeliveryInfo();
        }
        deliveryInfo.setName((String) map.get("name"));
        deliveryInfo.setEmail((String) map.get("email"));
        deliveryInfo.setPhoneNumber((String) map.get("phoneNumber"));
        deliveryInfo.setAddress((String) map.get("address"));
        return deliveryInfo;
    }

    private OrderStatus extractOrderStatus(Map<String, Object> map, OrderStatus status) {
        if (status == null) {
            status = new OrderStatus();
        }
        status.setAccepted((boolean) map.get("accepted"));
        status.setPaid((boolean) map.get("paid"));
        status.setDeliveryStatus(DeliveryStatus.valueOf((String) map.get("deliveryStatus")));
        return status;
    }

    private void extractAndAddOrderItem(Order order, DocumentSnapshot snapshot) {
        if (!snapshot.exists()) {
            return;
        }
        Product product = new Product(snapshot.getId());
        int quantity = snapshot.getLong("quantity").intValue();
        double orderPrice = snapshot.getDouble("orderPrice");
        order.addOrderItem(product, quantity, orderPrice);
    }

    private Map<String, Object> convertOrderToMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", firestore.collection("users").document(order.getUser().getId()));
        map.put("timestamp", order.getTimestamp());
        map.put("deliveryInfo", convertOrderDeliveryInfoToMap(order.getDeliveryInfo()));
        map.put("paymentMethod", order.getPaymentMethod().name());
        map.put("status", convertOrderStatusToMap(order.getStatus()));
        map.put("message", order.getMessage());
        return map;
    }

    private Map<String, Object> convertOrderDeliveryInfoToMap(OrderDeliveryInfo deliveryInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", deliveryInfo.getName());
        map.put("email", deliveryInfo.getEmail());
        map.put("phoneNumber", deliveryInfo.getPhoneNumber());
        map.put("address", deliveryInfo.getAddress());
        return map;
    }

    private Map<String, Object> convertOrderStatusToMap(OrderStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("accepted", status.isAccepted());
        map.put("paid", status.isPaid());
        map.put("deliveryStatus", status.getDeliveryStatus().name());
        return map;
    }

    private Map<String, Object> convertOrderItemToMap(OrderItem item) {
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", item.getQuantity());
        map.put("orderPrice", item.getOrderPrice());
        return map;
    }
}
