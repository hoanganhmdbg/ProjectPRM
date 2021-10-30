package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Entity.Gender;

import java.util.List;
import java.util.stream.Collectors;

public class GenderDao extends BaseDao implements IGenderDao {
    public GenderDao() {
        super();
    }

    public GenderDao(FirebaseFirestore firestore) {
        super(firestore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<List<Gender>> getAllGenders() {
        return firestore
                .collection("genders")
                .get()
                .continueWith(task -> task.getResult()
                        .getDocuments()
                        .stream()
                        .map(snapshot -> extractGender(snapshot, null))
                        .collect(Collectors.toList())
                ).continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Gender> getGenderById(String id) {
        return firestore
                .collection("genders")
                .document(id)
                .get()
                .continueWith(task -> extractGender(task.getResult(), null))
                .continueWith(wrapDaoException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Boolean> fetchGender(Gender gender) {
        return firestore
                .collection("genders")
                .document(gender.getId())
                .get()
                .continueWith(task -> extractGender(task.getResult(), gender) != null)
                .continueWith(wrapDaoException());
    }

    private Gender extractGender(DocumentSnapshot snapshot, Gender gender) {
        if (!snapshot.exists()) {
            return null;
        }
        if (gender == null) {
            gender = new Gender();
        }
        gender.setId(snapshot.getId());
        gender.setName(snapshot.getString("name"));
        return gender;
    }
}
