package com.phamhuong.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

import java.util.List;

public class BookAdapterRecommended extends RecyclerView.Adapter<BookAdapterRecommended.ViewHolder> {
    private Context context;
    private List<Book> bookList;
    private OnBookClickListener listener;

    public BookAdapterRecommended(Context context, List<Book> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_recommended, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvBookTitle.setText(book.getTitle());
        holder.tvAuthor.setText("by: " + book.getAuthor());
        holder.tvCategory.setText(book.getGenre());
        holder.tvDescription.setText(book.getSummary());

        Glide.with(context)
                .load(book.getCoverUrl())
                .placeholder(R.drawable.book_placeholder)
                .into(holder.imgBookCover);

        holder.btnDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCover;
        TextView tvBookTitle, tvAuthor, tvCategory, tvDescription;
        Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
}