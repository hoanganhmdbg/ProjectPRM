package com.project.prm391.shoesstore.Services;

/**
 * Created by nguyen on 3/22/2018.
 */

public interface INotificationService {
    void displayToast(String message);

    void displayToast(String message, boolean longDuration);

    void displayToastForException(Throwable throwable);

    void displayToastForException(Throwable throwable, boolean longDuration);
}
