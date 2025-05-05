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

public class BookRankAdapter extends RecyclerView.Adapter<BookRankAdapter.ViewHolder> {
    private Context context;
    private List<Book> books;
    private OnBookClickListener listener;
    private boolean isEbook; // true for ebook, false for audiobook

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookRankAdapter(Context context, List<Book> books, boolean isEbook, OnBookClickListener listener) {
        this.context = context;
        this.books = books;
        this.isEbook = isEbook;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        
        // Set rank number (position + 1 for 1-based ranking)
        holder.tvRankNumber.setText(String.valueOf(position + 1));
        
        // Set book title
        holder.tvBookTitle.setText(book.getTitle());
        
        // Set author
        holder.tvAuthor.setText(book.getAuthor());
        
        // Set book type
        holder.tvBookType.setText(book.getAudioUrl() != null ? "Sách nói" : "Sách điện tử");
        
        // Set rating
        if (book.getRating() > 0) {
            holder.tvRating.setText(String.format("%.1f", book.getRating()));
        } else {
            holder.tvRating.setText("N/A");
        }
        
        // Set price
        if (book.getPrice() > 0) {
            holder.tvPrice.setText(String.format("%,d₫", book.getPrice()));
        } else {
            holder.tvPrice.setText("Free");
        }

        // Load book cover
        Glide.with(context)
            .load(book.getCoverUrl())
            .placeholder(R.drawable.book_placeholder)
            .into(holder.imgBookCover);

        // Handle click events
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public void updateData(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRankNumber;
        ImageView imgBookCover;
        TextView tvBookTitle;
        TextView tvAuthor;
        TextView tvBookType;
        TextView tvRating;
        TextView tvPrice;

        ViewHolder(View itemView) {
            super(itemView);
            tvRankNumber = itemView.findViewById(R.id.tvRankNumber);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBookType = itemView.findViewById(R.id.tvBookType);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}