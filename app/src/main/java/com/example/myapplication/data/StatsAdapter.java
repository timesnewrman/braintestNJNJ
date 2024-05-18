package com.example.myapplication.data;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder>{

    FirebaseFirestore fireStore;
    LayoutInflater inflater;
    List users;

    public StatsAdapter(Context context, List<?> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        Log.i("StatsAdapter", "initialised stats adapter");
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        final ImageView avatar;
        final TextView userName;
        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.StatsitemImageView);
            this.userName = itemView.findViewById(R.id.StatsitemTextView);
            Log.i("StatsAdapter", "initialised stats viewHolder");
        }
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user, parent, false);
        Log.i("StatsAdapter", "created view holder");
        return new StatsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        UserStats user = (UserStats) users.get(position);

        holder.avatar.setImageBitmap(user.avatar);
        holder.userName.setText(user.username);

        Log.i("StatsAdapter", "binded view holder");
    }

    @Override
    public int getItemCount() {
        System.out.println("StatsAdapter: list size is " + users.size());
        return this.users.size();
    }
}