package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.User;
import com.bkv.tickets.Services.Interfaces.IUserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

public class FirestoreUserService implements IUserService {
    private static final String LOG_TAG = FirestoreUserService.class.getName();
    public static final String USERS_COLLECTION_PATH = "users";
    private static final String AUTH_ID_FIELD = "authId";
    private final FirebaseFirestore db;

    public FirestoreUserService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void create(@NonNull User user, @NonNull OnCompleteListener<User> onCompleteListener) {
        db.collection(USERS_COLLECTION_PATH)
                .add(user)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get user");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    Task<DocumentSnapshot> userTask = task.getResult().get();

                    if (!userTask.isSuccessful()) {
                        Exception exception = userTask.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get user");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    try {
                        onCompleteListener.onComplete(Tasks.forResult(parseUser(userTask.getResult())));
                    } catch (IllegalStateException e) {
                        Log.e(LOG_TAG, e.getMessage());
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    @Override
    public void update(@NonNull User user, @NonNull OnCompleteListener<Void> onCompleteListener) {
        db.collection(USERS_COLLECTION_PATH)
                .document(user.getId())
                .set(user, SetOptions.merge())
                .addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void getById(@NonNull String userId, @NonNull OnCompleteListener<User> onCompleteListener) {
        db.collection(USERS_COLLECTION_PATH)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get user");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    try {
                        User user = parseUser(task.getResult());

                        onCompleteListener.onComplete(Tasks.forResult(user));
                    } catch (IllegalStateException e) {
                        Log.e(LOG_TAG, e.getMessage());
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    @Override
    public void getByAuthId(@NonNull String authId, @NonNull OnCompleteListener<User> onCompleteListener) {
        db.collection(USERS_COLLECTION_PATH)
                .whereEqualTo(AUTH_ID_FIELD, authId)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get user");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    QuerySnapshot userQuery = task.getResult();

                    if (userQuery == null || userQuery.isEmpty()) {
                        onCompleteListener.onComplete(Tasks.forResult(null));
                    }

                    try {
                        onCompleteListener.onComplete(Tasks.forResult(parseUser(userQuery.getDocuments().get(0))));
                    } catch (NullPointerException | IllegalStateException e) {
                        Log.e(LOG_TAG, e.getMessage());
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    public static User parseUser(DocumentSnapshot userDocument) throws IllegalStateException{
        if (userDocument == null || !userDocument.exists()) {
            return null;
        }

        User user = userDocument.toObject(User.class);

        if (user == null) {
            Log.e(LOG_TAG, "toObject returned null");
            throw new IllegalStateException("Failed to parse User object");
        }

        user.setId(userDocument
                        .getId())
                .setReference(userDocument.getReference());

        return user;
    }
}
