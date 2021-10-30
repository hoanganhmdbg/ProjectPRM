package com.project.prm391.shoesstore.Entity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class SignInMethodStatus {
    private final boolean hasEmailSignIn;
    private final boolean hasGoogleSignIn;
    private final boolean hasFacebookSignIn;

    private SignInMethodStatus(boolean hasEmailSignIn, boolean hasGoogleSignIn, boolean hasFacebookSignIn) {
        this.hasEmailSignIn = hasEmailSignIn;
        this.hasGoogleSignIn = hasGoogleSignIn;
        this.hasFacebookSignIn = hasFacebookSignIn;
    }

    public static SignInMethodStatus fromProviderList(List<String> providers) {
        return new SignInMethodStatus(
                providers.contains(EmailAuthProvider.PROVIDER_ID),
                providers.contains(GoogleAuthProvider.PROVIDER_ID),
                providers.contains(FacebookAuthProvider.PROVIDER_ID)
        );
    }

    public boolean hasEmailSignIn() {
        return hasEmailSignIn;
    }

    public boolean hasGoogleSignIn() {
        return hasGoogleSignIn;
    }

    public boolean hasFacebookSignIn() {
        return hasFacebookSignIn;
    }

    public boolean isEmailRegistered() {
        return hasEmailSignIn || hasGoogleSignIn || hasFacebookSignIn;
    }
}
