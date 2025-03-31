package com.phamhuong.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private List<String> genres;
    private List<String> selectedGenres = new ArrayList<>();

    public GenreAdapter(List<String> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.tvGenre.setText(genre);
        holder.tvGenre.setSelected(selectedGenres.contains(genre));

        holder.tvGenre.setOnClickListener(v -> {
            if (selectedGenres.contains(genre)) {
                selectedGenres.remove(genre);
            } else {
                selectedGenres.add(genre);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public List<String> getSelectedGenres() {
        return selectedGenres;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }
    }
}
