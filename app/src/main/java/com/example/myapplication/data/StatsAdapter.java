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

import java.util.List;
import java.util.Objects;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder>{
    Context context;
    LayoutInflater inflater;
    List<?> users;
    String currentUserId;

    public StatsAdapter(Context context, List<?> users, String userId) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUserId = userId;
        Log.i("StatsAdapter", "initialised stats adapter");
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        final ImageView avatar;
        final TextView username;
        final TextView stars;
        final TextView number;
        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.StatsitemImageView);
            this.username = itemView.findViewById(R.id.StatsitemTextView);
            this.stars = itemView.findViewById(R.id.StatsitemStarCount);
            this.number = itemView.findViewById(R.id.StatsItemNumber);
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
        holder.username.setText(user.username);
        holder.stars.setText(user.stars == null ? "0" : user.stars.toString());
        holder.number.setText(String.valueOf(position+1));

        if (Objects.equals(user.getUserid(), currentUserId)) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.accent));
            holder.username.setTextColor(context.getColor(R.color.white));
            holder.stars.setTextColor(context.getColor(R.color.white));
            holder.number.setTextColor(context.getColor(R.color.white));
        } else {
            holder.itemView.setBackgroundColor(context.getColor(R.color.card_background));
            holder.username.setTextColor(context.getColor(R.color.dark));
            holder.stars.setTextColor(context.getColor(R.color.dark));
            holder.number.setTextColor(context.getColor(R.color.dark));
        }

        Log.i("StatsAdapter", "binded view holder");
    }

    @Override
    public int getItemCount() {
        System.out.println("StatsAdapter: list size is " + users.size());
        return this.users.size();
    }

    @NonNull
    public String toString(){
        return users.toString();
    }
}
