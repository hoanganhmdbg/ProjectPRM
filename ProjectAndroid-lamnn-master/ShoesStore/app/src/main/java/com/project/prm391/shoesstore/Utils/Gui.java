package com.project.prm391.shoesstore.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;

/**
 * Created by nguyen on 3/23/2018.
 */

public class Gui {
    public static AlertDialog buildSimpleAlertDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create();
    }

    public static ProgressDialog buildIndeterminateProgressDialog(Context context, String title, String message) {
        ProgressDialog dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }

    public static void showInputError(TextInputLayout layout, String error) {
        if (Strings.isNullOrEmpty(error)) {
            layout.setErrorEnabled(false);
            layout.setError("");
        } else {
            layout.setErrorEnabled(true);
            layout.setError(error);
        }
    }
}
