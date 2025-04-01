package com.phamhuong.library.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class BookAdapterTrending extends RecyclerView.Adapter<BookAdapterTrending.ViewHolder> {
    private Context context;
    private List<Book> bookList;

    public BookAdapterTrending(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookAdapterTrending.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_rounded, parent, false);
        return new BookAdapterTrending.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapterTrending.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        Glide.with(context).load(book.getCoverUrl()).into(holder.imgBookCover);

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
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

