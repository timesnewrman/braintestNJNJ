package com.example.myapplication.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.myapplication.R;

import java.util.Base64;
import java.util.Map;


public class UserStats {
    public Bitmap avatar = null;
    public String username;
    public Object stars;

    public UserStats() {}

    public UserStats(Base64 avatar, String username, int stars) {
        this.username = username;
        this.stars = stars;
        this.avatar = base64toAvatar(avatar.toString());
    }

    public UserStats(Bitmap avatar, String username, int stars) {
        this.username = username;
        this.stars = stars;
        this.avatar = avatar;
    }

    public UserStats(String username) {
        this.username = username;
    }

    public UserStats(Map<String, Object> map, Context context) {
        this.username = (String) map.getOrDefault("username", null);
        this.stars = map.getOrDefault("stars", 0);
        this.avatar = base64toAvatar((String) map.getOrDefault("avatar", null));
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public Object getStars() {
        return stars;
    }

    private Bitmap base64toAvatar(String encodedAvatar){
        if (encodedAvatar == null) return null;

        byte [] decoded = Base64.getDecoder().decode(encodedAvatar);
        return  BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
    }
}
