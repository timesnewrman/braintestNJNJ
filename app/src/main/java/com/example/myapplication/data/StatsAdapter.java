package com.example.myapplication.data;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.example.myapplication.data.UserData;
class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder>{

    FirebaseFirestore fireStore;
    LayoutInflater inflater;
    List users;

    StatsAdapter(Context context, List<UserData> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        final ImageView avatar;
        final TextView userName;
        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.StatsitemImageView);
            this.userName = itemView.findViewById(R.id.StatsitemTextView);
        }
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        UserData user = (UserData) users.get(position);

        holder.avatar.setImageBitmap(user.avatar);
        holder.userName.setText(user.username);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}
