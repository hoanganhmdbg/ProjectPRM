package com.project.prm391.shoesstore.DAO;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by nguyen on 3/18/2018.
 */

public class ProductDaoTest {
    private Context context;
    private FirebaseApp firebaseApp;
    private ProductDao productDao;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        firebaseApp = FirebaseApp.initializeApp(context);
        productDao = new ProductDao(FirebaseFirestore.getInstance(firebaseApp));
    }

    @Test
    public void getProductByIdTest() throws ExecutionException, InterruptedException {
        Product product = Tasks.await(productDao.getProductById("p5f1ITDLBBvobOOixeHR"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 1, 15, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals("p5f1ITDLBBvobOOixeHR", product.getId());
        assertEquals("adidas Originals NMD_R1 W Boost Pink White", product.getName());
        assertEquals(calendar.getTime(), product.getPostedTime());
        assertEquals("running", product.getCategory().getId());
        assertEquals("adidas", product.getBrand().getId());
        assertEquals("women", product.getGender().getId());
        assertEquals(7.5, product.getSize(), 0.0001);
        assertEquals(true, product.isAvailable());
        assertEquals(90, product.getCurrentPrice(), 0.0001);
        assertEquals(120, product.getOriginalPrice(), 0.0001);
    }

    @Test
    public void getProductByIdTest2() throws ExecutionException, InterruptedException {
        Product product = Tasks.await(productDao.getProductById("unknown"));
        assertEquals(null, product);
    }

    @Test
    public void fetchProductTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 1, 15, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Product product = new Product("p5f1ITDLBBvobOOixeHR");
        boolean success = Tasks.await(productDao.fetchProduct(product));
        assertTrue(success);
        assertEquals("p5f1ITDLBBvobOOixeHR", product.getId());
        assertEquals("adidas Originals NMD_R1 W Boost Pink White", product.getName());
        assertEquals(calendar.getTime(), product.getPostedTime());
        assertEquals("running", product.getCategory().getId());
        assertEquals("adidas", product.getBrand().getId());
        assertEquals("women", product.getGender().getId());
        assertEquals(7.5, product.getSize(), 0.0001);
        assertEquals(true, product.isAvailable());
        assertEquals(90, product.getCurrentPrice(), 0.0001);
        assertEquals(120, product.getOriginalPrice(), 0.0001);
    }

    @Test
    @Ignore
    public void createProductTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        Product product = new Product();
        product.setName("Nike PG 1 Elements");
        product.setDescription("Matching Paul George's athleticism is no easy task, but that's just what Nike set out to do with the PG 1. George's first signature shoe is focused on top-tier performance with the perfect blend of durability, cushioning, and comfort all in a lightweight silhouette.");
        calendar.set(2018, 2, 2, 14, 45, 0);
        product.setPostedTime(calendar.getTime());
        product.setCategory(new Category("basketball"));
        product.setBrand(new Brand("nike"));
        product.setGender(new Gender("men"));
        product.setSize(10);
        product.setAvailable(true);
        product.setCurrentPrice(110);
        product.setOriginalPrice(110);
        ProductReview review = product.addProductReview(new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        review.setTitle("My favorite basketball shoes");
        review.setContent("Awesome!");
        calendar.set(2018, 2, 5, 10, 50, 0);
        review.setPublishedTime(calendar.getTime());
        review.setRating(5);
        Tasks.await(productDao.createProduct(product));
        assertNotNull(product.getId());
    }

    @Test
    public void createOrUpdateProductReviewTest() throws ExecutionException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2018, 2, 16, 19, 45, 0);
        Product product = new Product("p5f1ITDLBBvobOOixeHR");
        ProductReview review = product.addProductReview(new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        review.setTitle("Average running shoes");
        review.setContent("A nice casual shoes, but not very suitable for running.");
        review.setPublishedTime(calendar.getTime());
        review.setRating(3);
        Tasks.await(productDao.createOrUpdateProductReview(review));
        Product product2 = new Product("p5f1ITDLBBvobOOixeHR");
        boolean success = Tasks.await(productDao.fetchProductReviews(product2));
        assertTrue(success);
        ProductReview review2 = product2.getProductReview(new User("gRVKalfRPpTNIoTYgwBIeUsgOyz1"));
        assertNotNull(review2);
        assertEquals("Average running shoes", review2.getTitle());
        assertEquals("A nice casual shoes, but not very suitable for running.", review2.getContent());
        assertEquals(calendar.getTime(), review2.getPublishedTime());
        assertEquals(3, review2.getRating());
    }
}
