package com.phamhuong.library.adapter.recycle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;

import java.util.List;

public class BookVerticalAdapter extends RecyclerView.Adapter<BookVerticalAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookVerticalAdapter(Context context, List<Book> books, OnBookClickListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_vertical, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCover;
        TextView tvBookTitle;
        TextView tvRating;
        TextView tvPrice;

        BookViewHolder(View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookClick(books.get(position));
                }
            });
        }
        @SuppressLint("DefaultLocale")
        void bind(Book book) {
            // Set book title
            tvBookTitle.setText(book.getTitle());

            // Set rating
            tvRating.setText(String.format("%.1f", book.getRating()));

            // Set price
            if (book.getPrice() > 0) {
                tvPrice.setText(String.format("%,d₫", book.getPrice()));
            } else {
                tvPrice.setText("Free");
            }

            // Load book cover image using Glide
            Glide.with(context)
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.book_placeholder)
                    .into(imgBookCover);
        }
    }
    // Method to update data
    public void updateData(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }
}