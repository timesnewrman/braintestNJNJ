package com.example.myapplication.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    LayoutInflater inflater;
    List<?> levels;
    public LevelAdapter(Context context, int[] list){
        this.levels = Collections.singletonList(list);
        Log.i("uhuhuh", Collections.singletonList(list).toString());
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {

        holder.stars.setText(String.valueOf(position));
        Random rand = new Random(position);
        holder.desc.setText(String.valueOf(rand.nextInt(position+1)));

    }

    @Override
    public int getItemCount() {
        return this.levels.size();
    }

    class LevelViewHolder extends RecyclerView.ViewHolder {
        final TextView stars;
        final TextView desc;
        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stars = itemView.findViewById(R.id.item_level_star);
            this.desc = itemView.findViewById(R.id.item_level_description);
        }
    }
}
