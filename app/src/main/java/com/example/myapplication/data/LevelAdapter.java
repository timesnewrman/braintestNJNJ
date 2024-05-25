package com.example.myapplication.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.dashboard.DashboardFragment;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private final View.OnClickListener clickListener;
    private final Context context;
    LayoutInflater inflater;
    List<?> levels;
    public LevelAdapter(Context context, List<?> list, View.OnClickListener clickListener){
        this.levels = list;
        Log.i("uhuhuh", Collections.singletonList(list).toString());
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.context = context;
    }
    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_level, parent, false);
        view.setOnClickListener(clickListener);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {

        holder.stars.setText(String.valueOf(position));
        Random rand = new Random(position);
        holder.desc.setText(String.valueOf(rand.nextInt(position+1)));

        if (position == levels.size()-1) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.accent));
        }
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
