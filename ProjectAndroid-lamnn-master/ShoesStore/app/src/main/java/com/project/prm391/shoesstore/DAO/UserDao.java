package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDao extends BaseDao implements IUserDao {
    public UserDao() {
        super();
    }

    public UserDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    public static User extractUser(DocumentSnapshot snapshot, User user) {
        if (!snapshot.exists()) {
            return null;
        }
        if (user == null) {
            user = new User();
        }
        user.setId(snapshot.getId());
        user.setName(snapshot.getString("name"));
        user.setEmail(snapshot.getString("email"));
        user.setPhoneNumber(snapshot.getString("phoneNumber"));
        user.setPhotoUrl(snapshot.getString("photoUrl"));
        user.setAddress(snapshot.getString("address"));
        return user;
    }

    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("phoneNumber", user.getPhoneNumber());
        map.put("photoUrl", user.getPhotoUrl());
        map.put("address", user.getAddress());
        return map;
    }

    public Task<List<User>> getAllUsers() {
        return firestore
                .collection("users")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractUser(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    @Override
    public Task<User> getUserById(String id) {
        return firestore
                .collection("users")
                .document(id)
                .get()
                .continueWith(task -> extractUser(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Boolean> fetchUser(User user) {
        return firestore
                .collection("users")
                .document(user.getId())
                .get()
                .continueWith(task -> extractUser(task.getResult(), user) != null)
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Void> createOrUpdateUser(User user) {
        Map<String, Object> map = convertUserToMap(user);
        return firestore
                .collection("users")
                .document(user.getId())
                .set(map)
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Void> updateUser(User user) {
        Map<String, Object> map = convertUserToMap(user);
        return firestore
                .collection("users")
                .document(user.getId())
                .update(map)
                .continueWith(wrapDaoException());
    }
}
