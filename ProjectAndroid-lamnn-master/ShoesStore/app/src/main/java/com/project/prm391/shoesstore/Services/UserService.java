package com.project.prm391.shoesstore.Services;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.prm391.shoesstore.DAO.IUserDao;
import com.project.prm391.shoesstore.Entity.SignInMethodStatus;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Enum.AuthActionResult;

public class UserService implements IUserService {
    private final FirebaseAuth auth;
    private final IUserDao userDao;
    private User currentUserProfile;

    public UserService(IUserDao userDao) {
        this(FirebaseAuth.getInstance(), userDao);
    }

    public UserService(FirebaseAuth auth, IUserDao userDao) {
        this.auth = auth;
        this.userDao = userDao;

        signOut();
    }

    @Override
    public User getCurrentUserProfile() {
        return currentUserProfile;
    }

    private synchronized void setCurrentUserProfile(User userProfile) {
        this.currentUserProfile = userProfile;
    }

    private Task<Void> reloadCurrentUserProfile() {
        if (isSignedIn()) {
            return userDao.getUserById(getCurrentFirebaseUser().getUid()).continueWith(task -> {
                setCurrentUserProfile(task.getResult());
                return null;
            });
        } else {
            return Tasks.call(() -> {
                setCurrentUserProfile(null);
                return null;
            });
        }
    }

    @Override
    public Task<AuthActionResult> updateCurrentUserProfile(User userProfile) {
        if (isSignedIn()) {
            userProfile.setId(getCurrentFirebaseUser().getUid());
            return userDao
                    .updateUser(userProfile)
                    .continueWith(task -> {
                        if (task.isSuccessful()) {
                            return AuthActionResult.SUCCESSFUL;
                        } else {
                            return AuthActionResult.SERVER_ERROR;
                        }
                    }).continueWithTask(reloadCurrentUserProfileTask());
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN)
                    .continueWithTask(reloadCurrentUserProfileTask());
        }
    }

    @Override
    public boolean isSignedIn() {
        return getCurrentFirebaseUser() != null;
    }

    @Override
    public Task<AuthActionResult> registerUserAndSignIn(String email, String password, String name) {
        return auth
                .createUserWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        User user = new User(firebaseUser.getUid());
                        user.setEmail(email);
                        user.setName(name);
                        return userDao.createOrUpdateUser(user)
                                .continueWith(task1 -> task1.isSuccessful()
                                        ? AuthActionResult.SUCCESSFUL
                                        : AuthActionResult.SERVER_ERROR
                                );
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            return Tasks.forResult(AuthActionResult.INVALID_PASSWORD);
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            return Tasks.forResult(AuthActionResult.INVALID_EMAIL);
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            return Tasks.forResult(AuthActionResult.REGISTER_EMAIL_ALREADY_EXIST);
                        } else {
                            return Tasks.forResult(AuthActionResult.SERVER_ERROR);
                        }
                    }
                }).continueWithTask(reloadCurrentUserProfileTask());
    }

    @Override
    public Task<SignInMethodStatus> getSignInMethods(String email) {
        return auth.fetchProvidersForEmail(email)
                .continueWith(task -> SignInMethodStatus.fromProviderList(task.getResult().getProviders()));
    }

    @Override
    public SignInMethodStatus getCurrentUserSignInMethods() {
        return isSignedIn() ? SignInMethodStatus.fromProviderList(getCurrentFirebaseUser().getProviders()) : null;
    }

    /**
     * Sign in with email and password.
     *
     * @param email    Email.
     * @param password Password.
     * @return A task that returns the current user.
     */
    @Override
    public Task<AuthActionResult> signInWithEmailAndPassword(String email, String password) {
        return getSignInMethods(email).continueWithTask(task -> {
            if (task.isSuccessful()) {
                SignInMethodStatus signInMethodStatus = task.getResult();
                if (signInMethodStatus.isEmailRegistered()) {
                    if (signInMethodStatus.hasEmailSignIn()) {
                        return signInWithCredential(EmailAuthProvider.getCredential(email, password));
                    } else {
                        return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_NOT_LINKED);
                    }
                } else {
                    return Tasks.forResult(AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST);
                }
            }
            return Tasks.forResult(AuthActionResult.SERVER_ERROR);
        }).continueWithTask(reloadCurrentUserProfileTask());
    }

    /**
     * Sign in with Google.
     * The Google ID token can be retrieved by using {@link GoogleSignInAccount#getIdToken()} method.
     *
     * @param idToken The Google ID token.
     * @return A task that returns the current user.
     */
    @Override
    public Task<AuthActionResult> signInWithGoogle(String email, String idToken) {
        return getSignInMethods(email).continueWithTask(task -> {
            if (task.isSuccessful()) {
                SignInMethodStatus signInMethodStatus = task.getResult();
                if (signInMethodStatus.isEmailRegistered()) {
                    if (signInMethodStatus.hasGoogleSignIn()) {
                        return signInWithCredential(GoogleAuthProvider.getCredential(idToken, null));
                    } else {
                        return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_NOT_LINKED);
                    }
                } else {
                    return Tasks.forResult(AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST);
                }
            }
            return Tasks.forResult(AuthActionResult.SERVER_ERROR);
        }).continueWithTask(reloadCurrentUserProfileTask());
    }

    /**
     * Sign in with Facebook.
     * The Facebook access token can be retrieved by using {@link AccessToken#getToken()} method.
     *
     * @param accessToken The Facebook access token.
     * @return A task that returns the current user.
     */
    @Override
    public Task<AuthActionResult> signInWithFacebook(String email, String accessToken) {
        return getSignInMethods(email).continueWithTask(task -> {
            if (task.isSuccessful()) {
                SignInMethodStatus signInMethodStatus = task.getResult();
                if (signInMethodStatus.isEmailRegistered()) {
                    if (signInMethodStatus.hasFacebookSignIn()) {
                        return signInWithCredential(FacebookAuthProvider.getCredential(accessToken));
                    } else {
                        return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_NOT_LINKED);
                    }
                } else {
                    return Tasks.forResult(AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST);
                }
            }
            return Tasks.forResult(AuthActionResult.SERVER_ERROR);
        }).continueWithTask(reloadCurrentUserProfileTask());
    }

    @Override
    public Task<AuthActionResult> linkWithGoogle(String idToken) {
        if (isSignedIn()) {
            if (getCurrentUserSignInMethods().hasGoogleSignIn()) {
                return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_ALREADY_LINKED);
            } else {
                return getCurrentFirebaseUser()
                        .linkWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                        .continueWith(task -> {
                            if (task.isSuccessful()) {
                                return AuthActionResult.SUCCESSFUL;
                            } else {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    return AuthActionResult.INVALID_OR_EXPIRED_TOKEN;
                                } else if (e instanceof FirebaseAuthUserCollisionException) {
                                    return AuthActionResult.INVALID_OR_EXPIRED_TOKEN;
                                } else {
                                    return AuthActionResult.UNKNOWN_ERROR;
                                }
                            }
                        });
            }
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN);
        }
    }

    @Override
    public Task<AuthActionResult> linkWithFacebook(String accessToken) {
        if (isSignedIn()) {
            if (getCurrentUserSignInMethods().hasFacebookSignIn()) {
                return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_ALREADY_LINKED);
            } else {
                return getCurrentFirebaseUser()
                        .linkWithCredential(FacebookAuthProvider.getCredential(accessToken))
                        .continueWith(task -> {
                            if (task.isSuccessful()) {
                                return AuthActionResult.SUCCESSFUL;
                            } else {
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    return AuthActionResult.INVALID_OR_EXPIRED_TOKEN;
                                } else if (e instanceof FirebaseAuthUserCollisionException) {
                                    return AuthActionResult.INVALID_OR_EXPIRED_TOKEN;
                                } else {
                                    return AuthActionResult.UNKNOWN_ERROR;
                                }
                            }
                        });
            }
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN);
        }
    }

    @Override
    public Task<AuthActionResult> unlinkGoogle() {
        if (isSignedIn()) {
            if (getCurrentUserSignInMethods().hasGoogleSignIn()) {
                return getCurrentFirebaseUser()
                        .unlink(GoogleAuthProvider.PROVIDER_ID)
                        .continueWith(task -> {
                            if (task.isSuccessful()) {
                                return AuthActionResult.SUCCESSFUL;
                            } else {
                                return AuthActionResult.UNKNOWN_ERROR;
                            }
                        });
            } else {
                return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_NOT_LINKED);
            }
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN);
        }
    }

    @Override
    public Task<AuthActionResult> unlinkFacebook() {
        if (isSignedIn()) {
            if (getCurrentUserSignInMethods().hasGoogleSignIn()) {
                return getCurrentFirebaseUser()
                        .unlink(FacebookAuthProvider.PROVIDER_ID)
                        .continueWith(task -> {
                            if (task.isSuccessful()) {
                                return AuthActionResult.SUCCESSFUL;
                            } else {
                                return AuthActionResult.UNKNOWN_ERROR;
                            }
                        });
            } else {
                return Tasks.forResult(AuthActionResult.SIGN_IN_METHOD_NOT_LINKED);
            }
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN);
        }
    }

    @Override
    public Task<AuthActionResult> sendPasswordResetEmail(String email) {
        return auth.sendPasswordResetEmail(email).continueWith(task -> {
            if (task.isSuccessful()) {
                return AuthActionResult.SUCCESSFUL;
            } else {
                Exception e = task.getException();
                if (e instanceof FirebaseAuthInvalidUserException) {
                    return AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST;
                } else {
                    return AuthActionResult.UNKNOWN_ERROR;
                }
            }
        });
    }

    @Override
    public Task<AuthActionResult> changePassword(String newPassword) {
        if (isSignedIn()) {
            return getCurrentFirebaseUser().updatePassword(newPassword)
                    .continueWith(task -> {
                        if (task.isSuccessful()) {
                            return AuthActionResult.SUCCESSFUL;
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthWeakPasswordException) {
                                return AuthActionResult.INVALID_PASSWORD;
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                return AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST;
                            } else if (e instanceof FirebaseAuthRecentLoginRequiredException) {
                                return AuthActionResult.SERVER_ERROR;
                            } else {
                                return AuthActionResult.UNKNOWN_ERROR;
                            }
                        }
                    });
        } else {
            return Tasks.forResult(AuthActionResult.ACTION_REQUIRE_SIGN_IN);
        }
    }

    @Override
    public void signOut() {
        auth.signOut();
        setCurrentUserProfile(null);
    }

    private FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }

    private Task<AuthActionResult> signInWithCredential(AuthCredential credential) {
        return auth.signInWithCredential(credential)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return AuthActionResult.SUCCESSFUL;
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            return AuthActionResult.SIGN_IN_EMAIL_NOT_EXIST;
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            if (credential instanceof EmailAuthCredential) {
                                return AuthActionResult.INCORRECT_PASSWORD;
                            } else {
                                return AuthActionResult.INVALID_OR_EXPIRED_TOKEN;
                            }
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            return AuthActionResult.SIGN_IN_METHOD_NOT_LINKED;
                        } else {
                            return AuthActionResult.UNKNOWN_ERROR;
                        }
                    }
                });
    }

    private Continuation<AuthActionResult, Task<AuthActionResult>> reloadCurrentUserProfileTask() {
        return task -> {
            AuthActionResult result = task.getResult();
            if (result == AuthActionResult.SUCCESSFUL) {
                return reloadCurrentUserProfile()
                        .continueWith(task1 -> {
                            if (task1.isSuccessful()) {
                                return AuthActionResult.SUCCESSFUL;
                            } else {
                                return AuthActionResult.SERVER_ERROR;
                            }
                        });
            } else {
                return Tasks.forResult(result);
            }
        };
    }
}
