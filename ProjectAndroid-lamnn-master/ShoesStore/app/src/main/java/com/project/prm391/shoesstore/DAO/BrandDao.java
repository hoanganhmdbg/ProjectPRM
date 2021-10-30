package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.Brand;

import java.util.List;
import java.util.stream.Collectors;

public class BrandDao extends BaseDao implements IBrandDao {
    public BrandDao() {
        super();
    }

    public BrandDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<List<Brand>> getAllBrands() {
        return firestore
                .collection("brands")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractBrand(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Brand> getBrandById(String id) {
        return firestore
                .collection("brands")
                .document(id)
                .get()
                .continueWith(task -> extractBrand(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Boolean> fetchBrand(Brand brand) {
        return firestore
                .collection("brands")
                .document(brand.getId())
                .get()
                .continueWith(task -> extractBrand(task.getResult(), brand) != null)
                .continueWith(wrapDaoException());
    }

    private Brand extractBrand(DocumentSnapshot snapshot, Brand brand) {
        if (!snapshot.exists()) {
            return null;
        }
        if (brand == null) {
            brand = new Brand();
        }
        brand.setId(snapshot.getId());
        brand.setName(snapshot.getString("name"));
        brand.setImageUrl(snapshot.getString("imageUrl"));
        return brand;
    }
}
