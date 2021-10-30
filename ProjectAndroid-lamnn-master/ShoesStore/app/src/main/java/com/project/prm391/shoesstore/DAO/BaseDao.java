package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.prm391.shoesstore.Exceptions.DaoException;

/**
 * Created by nguyen on 3/20/2018.
 */

public class BaseDao {
    protected final FirebaseFirestore firestore;

    public BaseDao() {
        this(FirebaseFirestore.getInstance());
    }

    public BaseDao(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public <T> Continuation<T, T> wrapDaoException() {
        return task -> {
            if (task.isSuccessful()) {
                return task.getResult();
            }
            throw new DaoException(task.getException());
        };
    }

    public Continuation<Object, Void> wrapDaoExceptionForVoidTask() {
        return task -> {
            if (task.isSuccessful()) {
                return null;
            }
            throw new DaoException(task.getException());
        };
    }
}
