package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDao extends BaseDao implements ICategoryDao {
    public CategoryDao() {
        super();
    }

    public CategoryDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<List<Category>> getAllCategories() {
        return firestore
                .collection("categories")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractCategory(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Category> getCategoryById(String id) {
        return firestore
                .collection("categories")
                .document(id)
                .get()
                .continueWith(task -> extractCategory(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Boolean> fetchCategory(Category category) {
        return firestore
                .collection("categories")
                .document(category.getId())
                .get()
                .continueWith(task -> extractCategory(task.getResult(), category) != null)
                .continueWith(wrapDaoException());
    }

    private Category extractCategory(DocumentSnapshot snapshot, Category category) {
        if (!snapshot.exists()) {
            return null;
        }
        if (category == null) {
            category = new Category();
        }
        category.setId(snapshot.getId());
        category.setName(snapshot.getString("name"));
        return category;
    }
}
