package com.example.myapplication.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.util.Base64;


public class UserData {
    public Bitmap avatar = null;
    public String username;
    public int stars;

    public UserData() {}

    public UserData(Base64 avatar, String username, int stars) {
        this.username = username;
        this.stars = stars;
        byte [] decoded = Base64.getDecoder().decode(avatar.toString());
        this.avatar = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
    }

    public UserData(Bitmap avatar, String username, int stars) {
        this.username = username;
        this.stars = stars;
        this.avatar = avatar;
    }

    public UserData(String username) {
        this.username = username;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public int getStars() {
        return stars;
    }
}
