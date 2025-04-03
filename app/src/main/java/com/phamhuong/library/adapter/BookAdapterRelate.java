package com.phamhuong.library.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.fragment.BookFragment;
import com.phamhuong.library.model.Book;

import java.util.List;

public class BookAdapterRelate extends RecyclerView.Adapter<BookAdapterRelate.ViewHolder> {
    private Context context;
    private List<Book> bookList;

    public BookAdapterRelate(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_relate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        // Load cover image using Glide
        Glide.with(context).load(book.getCoverUrl()).into(holder.imgBookCover);

        // Set book name and rating
        holder.tvBookName.setText(book.getTitle());
        //float rating = book.getRating();
        //holder.tvRatingText.setText(String.valueOf(rating)); // assuming getAverageRating returns a float

        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookFragment(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCover;
        TextView tvBookName, tvRatingText;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
        }
    }

    private void openBookFragment(Book book) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        BookFragment bookFragment = new BookFragment();

        // Gửi đối tượng Book qua Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bookFragment.setArguments(bundle);

        transaction.replace(R.id.content_frame, bookFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
