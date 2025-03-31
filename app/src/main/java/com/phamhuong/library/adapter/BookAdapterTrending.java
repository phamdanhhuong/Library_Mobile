package com.phamhuong.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_recommended, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return new BookAdapterTrending.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapterTrending.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        Glide.with(context).load(book.getCoverUrl()).into(holder.imgBookCover);
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
}

