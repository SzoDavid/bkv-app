package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.User;
import com.bkv.tickets.Services.Interfaces.IUserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class FirestoreUserService implements IUserService {
    private static final String LOG_TAG = FirestoreUserService.class.getName();
    private static final String USERS_COLLECTION_PATH = "users";
    private final FirebaseFirestore db;

    public FirestoreUserService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void create(@NonNull User user, @NonNull OnCompleteListener<DocumentReference> onCompleteListener) {
        db.collection(USERS_COLLECTION_PATH)
                .add(user)
                .addOnCompleteListener(onCompleteListener);
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

                    DocumentSnapshot userDocument = task.getResult();

                    if (!userDocument.exists()) {
                        onCompleteListener.onComplete(Tasks.forResult(null));
                    }

                    User user = userDocument.toObject(User.class);

                    if (user == null) {
                        IllegalStateException exception = new IllegalStateException("Failed to parse User object");
                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                    }

                    onCompleteListener.onComplete(Tasks.forResult(user));
                });
    }
}
