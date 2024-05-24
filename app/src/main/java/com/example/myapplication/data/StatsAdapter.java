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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder>{
    Context context;
    LayoutInflater inflater;
    List<?> users;
    UserStats currentUser;

    public StatsAdapter(Context context, List<?> users, UserStats currentUser) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        Log.i("StatsAdapter", "initialised stats adapter");
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        final ImageView avatar;
        final TextView username;
        final TextView stars;
        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.StatsitemImageView);
            this.username = itemView.findViewById(R.id.StatsitemTextView);
            this.stars = itemView.findViewById(R.id.StatsitemStarCount);
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

        if (user == currentUser) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.accent));
            holder.username.setTextColor(context.getColor(R.color.white));
            holder.stars.setTextColor(context.getColor(R.color.white));
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
