package com.project.prm391.shoesstore.Utils;

import android.util.Patterns;

import com.project.prm391.shoesstore.Enum.InputValidationResult;

/**
 * Created by nguyen on 3/22/2018.
 */

public class InputValidation {
    public static InputValidationResult validateEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return InputValidationResult.EMPTY_STRING;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return InputValidationResult.INVALID_EMAIL;
        }
        return InputValidationResult.VALID;
    }

    public static InputValidationResult validatePassword(String password, int minimumLength) {
        if (Strings.isNullOrEmpty(password)) {
            return InputValidationResult.EMPTY_STRING;
        }
        if (password.length() < minimumLength) {
            return InputValidationResult.PASSWORD_LENGTH_TOO_SHORT;
        }
        return InputValidationResult.VALID;
    }

    public static InputValidationResult validateName(String name) {
        return validateNonEmptyString(name);
    }

    public static InputValidationResult validateConfirmPassword(String confirmPassword, String password) {
        if (Strings.isNullOrEmpty(confirmPassword)) {
            return InputValidationResult.EMPTY_STRING;
        }
        if (!confirmPassword.equals(password)) {
            return InputValidationResult.CONFIRM_PASSWORD_NOT_MATCH;
        }
        return InputValidationResult.VALID;
    }

    public static InputValidationResult validatePhoneNumber(String phoneNumber) {
        if (Strings.isNullOrEmpty(phoneNumber)) {
            return InputValidationResult.EMPTY_STRING;
        }
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            return InputValidationResult.INVALID_PHONE_NUMBER;
        }
        return InputValidationResult.VALID;
    }

    public static InputValidationResult validateAddress(String address) {
        return validateNonEmptyString(address);
    }

    public static InputValidationResult validateNonEmptyString(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return InputValidationResult.EMPTY_STRING;
        }
        return InputValidationResult.VALID;
    }
}
