package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.project.prm391.shoesstore.Entity.SignInMethodStatus;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Enum.AuthActionResult;

/**
 * Created by nguyen on 3/21/2018.
 */

public interface IUserService {
    User getCurrentUserProfile();

    Task<AuthActionResult> updateCurrentUserProfile(User userProfile);

    boolean isSignedIn();

    Task<AuthActionResult> registerUserAndSignIn(String email, String password, String name);

    Task<SignInMethodStatus> getSignInMethods(String email);

    SignInMethodStatus getCurrentUserSignInMethods();

    Task<AuthActionResult> signInWithEmailAndPassword(String email, String password);

    Task<AuthActionResult> signInWithGoogle(String email, String idToken);

    Task<AuthActionResult> signInWithFacebook(String email, String accessToken);

    Task<AuthActionResult> linkWithGoogle(String idToken);

    Task<AuthActionResult> linkWithFacebook(String accessToken);

    Task<AuthActionResult> unlinkGoogle();

    Task<AuthActionResult> unlinkFacebook();

    Task<AuthActionResult> sendPasswordResetEmail(String email);

    Task<AuthActionResult> changePassword(String newPassword);

    void signOut();
}
