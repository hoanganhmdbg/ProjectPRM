package com.project.prm391.shoesstore.DAO;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.User;

import java.util.List;

/**
 * Notes:
 * <ul>
 * <li>The functions of this interface is to retrieve and update user profile.
 * It does not handle login and authentication functions.</li>
 * </ul>
 */
public interface IUserDao {
    /**
     * Get the list of users.
     *
     * @return A task that returns the list of users.
     */
    Task<List<User>> getAllUsers();

    /**
     * Get user by ID.
     *
     * @param id ID of the user.
     * @return A task that returns the user, or null if ID does not exist.
     */
    Task<User> getUserById(String id);

    /**
     * Fetch information to the {@link User} object.<br/>
     * The ID of the object must be already set.<br/>
     *
     * @param user The {@link User} object.
     * @return A task that returns a boolean indicating whether the action was succeed or failed.
     */
    Task<Boolean> fetchUser(User user);

    /**
     * Create a new user if it does not exist, or update the existing user.
     *
     * @param user The {@link User} object.
     * @return A task that returns nothing.
     */
    Task<Void> createOrUpdateUser(User user);

    /**
     * Update an existing user.
     *
     * @param user The {@link User} object.
     * @return A task that returns nothing.
     */
    Task<Void> updateUser(User user);
}
