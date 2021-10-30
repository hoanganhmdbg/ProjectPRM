package com.project.prm391.shoesstore.Services;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.DAO.ProductDao;
import com.project.prm391.shoesstore.DAO.UserDao;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nguyen on 3/25/2018.
 */

public class ReviewServiceTest {
    private Context context;
    private FirebaseApp firebaseApp;
    private FirebaseAuth firebaseAuth;
    private UserDao userDao;
    private ProductDao productDao;
    private ReviewService reviewService;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        firebaseApp = FirebaseApp.initializeApp(context);
        userDao = new UserDao(FirebaseFirestore.getInstance(firebaseApp));
        productDao = new ProductDao(FirebaseFirestore.getInstance(firebaseApp));
        reviewService = new ReviewService(productDao, userDao);
    }

    @Test
    public void fetchProductReviewsTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 18, 9, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Product product = new Product("y5CbT1NSOZOgzzYfny8f");
        Set<ProductReview> reviews = Tasks.await(reviewService.fetchProductReviews(product));
        assertEquals(1, reviews.size());
        ProductReview review = reviews.stream().findFirst().get();
        User user = review.getUser();
        assertEquals("gRVKalfRPpTNIoTYgwBIeUsgOyz1", user.getId());
        assertNotNull(user.getEmail());
        assertEquals("Very good product", review.getTitle());
        assertEquals("I like it!", review.getContent());
        assertEquals(calendar.getTime(), review.getPublishedTime());
        assertEquals(5, review.getRating());
    }
}
