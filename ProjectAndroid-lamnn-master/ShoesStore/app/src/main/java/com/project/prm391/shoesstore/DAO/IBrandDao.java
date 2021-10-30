package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.Brand;

import java.util.List;

public interface IBrandDao {
    /**
     * Get the brand list.
     *
     * @return A task that returns the list of brands.
     */
    Task<List<Brand>> getAllBrands();

    /**
     * Get brand by ID.
     *
     * @param id ID of the brand.
     * @return A task that returns the brand, or null if ID does not exist.
     */
    Task<Brand> getBrandById(String id);

    /**
     * Fetch information to the {@link Brand} object.
     * The ID of the object must be already set.
     *
     * @param brand The {@link Brand} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchBrand(Brand brand);
}
