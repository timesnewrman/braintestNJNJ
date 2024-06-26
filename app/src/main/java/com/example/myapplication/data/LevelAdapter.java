package com.example.myapplication.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Arrays;
import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private final View.OnClickListener clickListener;
    private final Context context;
    LayoutInflater inflater;
    List<?> levels;
    public LevelAdapter(Context context, List<?> list, View.OnClickListener clickListener){
        this.levels = list;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {

        holder.levelNumber.setText(String.valueOf(position+1));
        holder.desc.setText(
                        context.getString(R.string.level_item_difficultytext)
                        + (position % 19 + 3) + " "
                        + Arrays.asList(
                                context.getString(R.string.blitz_card),
                                context.getString(R.string.blitz_fast)
                        ).get(position%2)
        );

        if (position == levels.size()-1) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.accent));
        } else {
            holder.itemView.setBackgroundColor(context.getColor(R.color.card_background));
        }
    }

    @Override
    public int getItemCount() {
        return this.levels.size();
    }

    static class LevelViewHolder extends RecyclerView.ViewHolder {
        final TextView levelNumber;
        final TextView desc;
        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            this.levelNumber = itemView.findViewById(R.id.item_level_star);
            this.desc = itemView.findViewById(R.id.item_level_description);
        }
    }
}
