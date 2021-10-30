package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Category;

import java.util.List;

public interface ICategoryDao {
    /**
     * Get the list of categories.
     *
     * @return A task that returns the list of categories.
     */
    Task<List<Category>> getAllCategories();

    /**
     * Get category by ID.
     *
     * @param id ID of the category.
     * @return A task that returns the category, or null if ID does not exist.
     */
    Task<Category> getCategoryById(String id);

    /**
     * Fetch information to the {@link Category} object.
     * The ID of the object must be already set.
     *
     * @param category The {@link Category} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchCategory(Category category);
}
