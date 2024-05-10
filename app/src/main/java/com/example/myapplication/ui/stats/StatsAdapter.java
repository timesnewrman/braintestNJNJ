package com.example.myapplication.ui.stats;


import android.content.Context;
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

class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder>{

    FirebaseFirestore fireStore;
    LayoutInflater inflater;
    List<userData> users;

    StatsAdapter(Context context, List<> users) {
        this.users = states;
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
