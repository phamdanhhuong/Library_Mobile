package com.phamhuong.library.adapter.book;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

import java.util.List;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioBookAdapter.AudioBookViewHolder> {

    private Context context;
    private List<Book> audioBooks;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book audiobook);
    }

    public AudioBookAdapter(Context context, List<Book> audioBooks, OnItemClickListener listener) {
        this.context = context;
        this.audioBooks = audioBooks;
        this.listener = listener;
    }

    public void setAudioBooks(List<Book> audioBooks) {
        this.audioBooks = audioBooks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AudioBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_audio_book, parent, false); // Tạo layout cho mỗi item
        return new AudioBookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioBookViewHolder holder, int position) {
        Book currentBook = audioBooks.get(position);
        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthor());

        if (currentBook.getCoverUrl() != null && !currentBook.getCoverUrl().isEmpty()) {
            Glide.with(context)
                    .load(currentBook.getCoverUrl())
                    .into(holder.coverImageView);
        } else {
            holder.coverImageView.setImageResource(R.drawable.book_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentBook);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioBooks.size();
    }

    public static class AudioBookViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImageView;
        TextView titleTextView;
        TextView authorTextView;

        public AudioBookViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.audioBookCoverImageView);
            titleTextView = itemView.findViewById(R.id.audioBookTitleTextView);
            authorTextView = itemView.findViewById(R.id.audioBookAuthorTextView);
        }
    }
}