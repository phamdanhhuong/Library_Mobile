package com.phamhuong.library.adapter.recycle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookBasketAdapter extends RecyclerView.Adapter<BookBasketAdapter.BookViewHolder>{
    private Context context;
    private List<Book> books;
    private Set<Integer> selectedPositions = new HashSet<>();
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public BookBasketAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookBasketAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false);
        return new BookBasketAdapter.BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookBasketAdapter.BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);

        if (selectedPositions.contains(position)) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_item_background));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getBindingAdapterPosition();
                if (selectedPositions.contains(adapterPosition)) {
                    selectedPositions.remove(adapterPosition);
                } else {
                    selectedPositions.add(adapterPosition);
                }
                notifyItemChanged(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public List<Book> getSelectedBooks() {
        List<Book> selectedBooks = new ArrayList<>();
        for (Integer pos : selectedPositions) {
            if (pos >= 0 && pos < books.size()) {
                selectedBooks.add(books.get(pos));
            }
        }
        return selectedBooks;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCover;
        TextView tvBookTitle;
        TextView tvAuthor;
        TextView tvRating;
        TextView tvPrice;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }

        @SuppressLint("DefaultLocale")
        void bind(Book book) {
            // Set book title
            tvBookTitle.setText(book.getTitle());

            // Set author
            tvAuthor.setText(book.getAuthor());

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
    private void openBookFragment(Book book) {
//        FragmentActivity activity = (FragmentActivity) context;
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        BookFragment bookFragment = new BookFragment();
//
//        // Gửi đối tượng Book qua Bundle
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("book", book);
//        bookFragment.setArguments(bundle);
//
//        transaction.replace(R.id.content_frame, bookFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }
    // Method to update data
    public void updateData(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }
}
