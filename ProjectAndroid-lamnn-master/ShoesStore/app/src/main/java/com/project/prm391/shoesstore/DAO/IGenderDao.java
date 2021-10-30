package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Gender;

import java.util.List;

public interface IGenderDao {
    /**
     * Get the list of genders.
     *
     * @return A task that returns the list of categories.
     */
    Task<List<Gender>> getAllGenders();

    /**
     * Get gender by ID.
     *
     * @param id ID of the gender.
     * @return A task that returns the gender, or null if ID does not exist.
     */
    Task<Gender> getGenderById(String id);

    /**
     * Fetch information to the {@link Gender} object.
     * The ID of the object must be already set.
     *
     * @param gender The {@link Gender} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchGender(Gender gender);
}
