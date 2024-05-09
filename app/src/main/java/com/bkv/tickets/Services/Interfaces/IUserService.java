package com.bkv.tickets.Services.Interfaces;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

public interface IUserService {
    void create(@NonNull User user, @NonNull OnCompleteListener<DocumentReference> onCompleteListener);
    void update(@NonNull User user, @NonNull OnCompleteListener<Void> onCompleteListener);
    void getById(@NonNull String userId, @NonNull OnCompleteListener<User> onCompleteListener);
    void getByAuthId(@NonNull String authId, @NonNull OnCompleteListener<User> onCompleteListener);
}
