package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDao extends BaseDao implements IProductDao {
    public ProductDao() {
        super();
    }

    public ProductDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public Task<List<Product>> getAllProducts() {
        return firestore
                .collection("products")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractProduct(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    @Override
    public Task<Product> getProductById(String id) {
        return firestore
                .collection("products")
                .document(id)
                .get()
                .continueWith(task -> extractProduct(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Boolean> fetchProduct(Product product) {
        return firestore
                .collection("products")
                .document(product.getId())
                .get()
                .continueWith(task -> extractProduct(task.getResult(), product) != null)
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Void> createProduct(Product product) {
        Map<String, Object> map = convertProductToMap(product);
        return firestore
                .collection("products")
                .add(map)
                .continueWith(task -> {
                    String id = task.getResult().getId();
                    product.setId(id);
                    return product;
                }).continueWithTask(task -> createProductReviews(task.getResult()));
    }

    private Task<Void> createProductReviews(Product product) {
        CollectionReference collectionRef = firestore.collection("products").document(product.getId()).collection("reviews");
        WriteBatch batch = firestore.batch();
        product.getProductReviews().forEach(review -> batch.set(collectionRef.document(review.getUser().getId()), convertProductReviewToMap(review)));
        return batch.commit();
    }

    @Override
    public Task<Boolean> fetchProductReviews(Product product) {
        return firestore
                .collection("products")
                .document(product.getId())
                .get()
                .continueWithTask(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        return Tasks.forResult(false);
                    }
                    return documentSnapshot.getReference()
                            .collection("reviews")
                            .get()
                            .continueWith(task1 -> {
                                QuerySnapshot querySnapshot = task1.getResult();
                                product.clearProductReviews();
                                querySnapshot.forEach(snapshot -> extractAndAddProductReview(product, snapshot));
                                return true;
                            });
                });
    }

    @Override
    public Task<Void> createOrUpdateProductReview(ProductReview review) {
        Map<String, Object> map = convertProductReviewToMap(review);
        Map<String, Object> averageRatingsUpdateMap = new HashMap<>();
        averageRatingsUpdateMap.put("averageRatings", review.getProduct().getAverageRatings());
        WriteBatch batch = firestore.batch();
        batch.set(firestore
                .collection("products")
                .document(review.getProduct().getId())
                .collection("reviews")
                .document(review.getUser().getId()), map);
        batch.update(firestore
                .collection("products")
                .document(review.getProduct().getId()), averageRatingsUpdateMap);
        return batch.commit();
    }

    @Override
    public Task<List<Product>> getUserWishList(User user) {
        return firestore
                .collection("users")
                .document(user.getId())
                .collection("wishlist")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(documentSnapshot -> new Product(documentSnapshot.getId()))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    @Override
    public Task<Boolean> checkIfProductInUserWishList(User user, Product product) {
        return firestore
                .collection("users")
                .document(user.getId())
                .collection("wishlist")
                .document(product.getId())
                .get()
                .continueWith(task -> task.getResult().exists())
                .continueWith(wrapDaoException());
    }

    @Override
    public Task<Boolean> addProductToUserWishList(User user, Product product) {
        return firestore.runTransaction(transaction -> {
            DocumentReference ref = firestore.collection("users")
                    .document(user.getId())
                    .collection("wishlist")
                    .document(product.getId());
            boolean exists = transaction.get(ref).exists();
            if (exists) {
                return false;
            } else {
                transaction.set(ref, new HashMap<>());
                return true;
            }
        });
    }

    @Override
    public Task<Boolean> removeProductFromUserWishList(User user, Product product) {
        return firestore.runTransaction(transaction -> {
            DocumentReference ref = firestore.collection("users")
                    .document(user.getId())
                    .collection("wishlist")
                    .document(product.getId());
            boolean exists = transaction.get(ref).exists();
            if (exists) {
                transaction.delete(ref);
                return true;
            } else {
                return false;
            }
        });
    }

    private void extractAndAddProductReview(Product product, DocumentSnapshot snapshot) {
        if (!snapshot.exists()) {
            return;
        }
        User user = new User(snapshot.getId());
        ProductReview review = product.addProductReview(user);
        review.setTitle(snapshot.getString("title"));
        review.setContent(snapshot.getString("content"));
        review.setPublishedTime(snapshot.getDate("publishedTime"));
        review.setRating(snapshot.getLong("rating").intValue());
    }

    private Product extractProduct(DocumentSnapshot snapshot, Product product) {
        if (!snapshot.exists()) {
            return null;
        }
        if (product == null) {
            product = new Product();
        }
        product.setId(snapshot.getId());
        product.setName(snapshot.getString("name"));
        product.setDescription(snapshot.getString("description"));
        product.setPostedTime(snapshot.getDate("postedTime"));
        product.setImageUrls((List<String>) snapshot.get("imageUrls"));
        product.setCategory(new Category(snapshot.getDocumentReference("category").getId()));
        product.setBrand(new Brand(snapshot.getDocumentReference("brand").getId()));
        product.setGender(new Gender(snapshot.getDocumentReference("gender").getId()));
        product.setSize(snapshot.getDouble("size"));
        product.setAvailable(snapshot.getBoolean("available"));
        product.setCurrentPrice(snapshot.getDouble("currentPrice"));
        product.setOriginalPrice(snapshot.getDouble("originalPrice"));
        product.setAverageRatings(snapshot.getDouble("averageRatings"));

        return product;
    }

    private Map<String, Object> convertProductToMap(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("description", product.getDescription());
        map.put("postedTime", product.getPostedTime());
        map.put("category", firestore.collection("categories").document(product.getCategory().getId()));
        map.put("brand", firestore.collection("brands").document(product.getBrand().getId()));
        map.put("gender", firestore.collection("genders").document(product.getGender().getId()));
        map.put("imageUrls", product.getImageUrls());
        map.put("size", product.getSize());
        map.put("available", product.isAvailable());
        map.put("currentPrice", product.getCurrentPrice());
        map.put("originalPrice", product.getOriginalPrice());
        map.put("averageRatings", product.getAverageRatings());
        return map;
    }

    private Map<String, Object> convertProductReviewToMap(ProductReview review) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", review.getTitle());
        map.put("content", review.getContent());
        map.put("publishedTime", review.getPublishedTime());
        map.put("rating", review.getRating());
        return map;
    }
}
