package com.project.prm391.shoesstore.DAO;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.User;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {
    private Context context;
    private FirebaseApp firebaseApp;
    private UserDao userDao;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        firebaseApp = FirebaseApp.initializeApp(context);
        userDao = new UserDao(FirebaseFirestore.getInstance(firebaseApp));
    }

    @Test
    public void getUserByIdTest() throws ExecutionException, InterruptedException {
        User user = Tasks.await(userDao.getUserById("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", user.getId());
        assertEquals("Lê Cao Nguyên", user.getName());
        assertEquals("nguyenlc1993@gmail.com", user.getEmail());
        assertEquals("01296685555", user.getPhoneNumber());
        assertEquals("Tổ 9, Khu 1, Phường Hồng Hải, Hạ Long, Quảng Ninh", user.getAddress());
        assertEquals("https://i.imgur.com/fP3CqYL.png", user.getPhotoUrl());
    }

    @Test
    public void updateUserTest() throws ExecutionException, InterruptedException {
        User user = new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1");
        user.setName("Lê Cao Nguyên");
        user.setEmail("nguyenlc1993@gmail.com");
        user.setPhoneNumber("01296685555");
        user.setPhotoUrl("https://i.imgur.com/fP3CqYL.png");
        user.setAddress("Tổ 9, Khu 1, Phường Hồng Hải, Hạ Long, Quảng Ninh");
        Tasks.await(userDao.updateUser(user));
        User user2 = Tasks.await(userDao.getUserById("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", user2.getId());
        assertEquals("Lê Cao Nguyên", user2.getName());
        assertEquals("nguyenlc1993@gmail.com", user2.getEmail());
        assertEquals("01296685555", user2.getPhoneNumber());
        assertEquals("Tổ 9, Khu 1, Phường Hồng Hải, Hạ Long, Quảng Ninh", user2.getAddress());
        assertEquals("https://i.imgur.com/fP3CqYL.png", user2.getPhotoUrl());
    }
}
