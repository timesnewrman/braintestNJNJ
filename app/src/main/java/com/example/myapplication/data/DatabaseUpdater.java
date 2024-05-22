package com.example.myapplication.data;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DatabaseUpdater {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    String userID = Objects.requireNonNull(fireAuth.getCurrentUser()).getUid();
    public DatabaseUpdater(Context context) {
    }

    public void upload(String key, Object thing) throws Exception{
        database.collection("users")
                .document(userID)
                .update(key, thing)
                .addOnFailureListener(err -> {
                    throw new RuntimeException(err);
                });
    }

    public void create(Object thing){
        database.collection("users")
                .document(userID)
                .set(thing)
                .addOnFailureListener(err -> {
                    throw new RuntimeException(err);
                });
    }

    public void increment(int stars) {
        database.collection("users")
                .document(userID)
                .update("stars", FieldValue.increment(stars))
                .addOnFailureListener(err -> {
                    throw new RuntimeException(err);
                });
    }
}
