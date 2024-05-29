package com.example.myapplication.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DatabaseUpdater {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final Context context;
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    String userID = Objects.requireNonNull(fireAuth.getCurrentUser()).getUid();
    public DatabaseUpdater(Context context) {
        this.context = context;
    }

    public void upload(String key, Object thing) throws Exception{
        checkUser();
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

    public void increment(int stars, Intent intent) {
        checkUser();

        Level.scenario scenario = (Level.scenario) Objects.requireNonNull(intent.getExtras()).get("scenario");
        assert scenario != null;

        Log.i("DatabaseUpdater", scenario.name());

        if (Objects.equals(scenario,Level.scenario.FROM_CHALLENGE)) {
            stars *= 3;
        }
        if (!Objects.equals(scenario,Level.scenario.NO_STARS)) database.collection("users")
                .document(userID)
                .update("stars", FieldValue.increment(stars))
                .addOnFailureListener(err -> {
                    throw new RuntimeException(err);
                }); else {
            Toast.makeText(context, context.getString(R.string.training_warning), Toast.LENGTH_LONG).show();
        }
        if (Objects.equals(scenario,Level.scenario.FROM_DASHBOARD)|Objects.equals(scenario,Level.scenario.LAUNCH_TUTORIAL)) {
            SharedPreferences data = context.getSharedPreferences("level", Context.MODE_PRIVATE);
            data.edit().putInt("level", data.getInt("level",2)+1).apply();
        }

    }

    private void checkUser() {
        if (Objects.isNull(fireAuth.getCurrentUser()))
            throw new RuntimeException("current user is null");
    }
}
