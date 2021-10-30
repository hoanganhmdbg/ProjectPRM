package com.project.prm391.shoesstore.Services;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.DAO.UserDao;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Enum.AuthActionResult;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Created by nguyen on 3/21/2018.
 */

public class UserServiceTest {
    private Context context;
    private FirebaseApp firebaseApp;
    private UserDao userDao;
    private FirebaseAuth firebaseAuth;
    private UserService userService;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        firebaseApp = FirebaseApp.initializeApp(context);
        userDao = new UserDao(FirebaseFirestore.getInstance(firebaseApp));
        firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        userService = new UserService(firebaseAuth, userDao);
    }

    @Test
    public void signInWithEmailAndPasswordTest() throws ExecutionException, InterruptedException {
        AuthActionResult result = Tasks.await(userService.signInWithEmailAndPassword("nguyenlc1993@gmail.com", "26121993"));
        assertEquals(AuthActionResult.SUCCESSFUL, result);
        assertEquals(true, userService.isSignedIn());
        User user = userService.getCurrentUserProfile();
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", user.getId());
        userService.signOut();
        assertEquals(false, userService.isSignedIn());
        assertEquals(null, userService.getCurrentUserProfile());
    }
}
