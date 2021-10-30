package com.project.prm391.shoesstore.Services;

import android.content.Context;
import android.widget.Toast;

import com.project.prm391.shoesstore.R;

/**
 * Created by nguyen on 3/22/2018.
 */

public class NotificationService implements INotificationService {
    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
    }

    @Override
    public void displayToast(String message) {
        displayToast(message, false);
    }

    @Override
    public void displayToast(String message, boolean longDuration) {
        Toast.makeText(context, message, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayToastForException(Throwable throwable) {
        displayToastForException(throwable, false);
    }

    @Override
    public void displayToastForException(Throwable throwable, boolean longDuration) {
        displayToast(context.getString(R.string.msg_exception_toast_format, throwable.getMessage()), longDuration);
    }
}
