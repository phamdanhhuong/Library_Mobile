package com.phamhuong.library.adapter;

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
import com.phamhuong.library.model.Category;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>{

    private Context context;
    private List<Book> ListBook;

    public BookAdapter(Context context, List<Book> ListBook) {
        this.context = context;
        this.ListBook = ListBook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book, parent, false);
        return new BookAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = ListBook.get(position);
        holder.txtGenre.setText(book.getGenre());
        holder.txtAuthor.setText(book.getAuthor());

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(book.getCoverUrl())
                .into(holder.imgCategory);
    }

    @Override
    public int getItemCount() {
        return ListBook.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtGenre;
        TextView txtAuthor;
        ImageView imgCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.tvBookName);
            txtAuthor = itemView.findViewById(R.id.tvBookAuthor);
            imgCategory = itemView.findViewById(R.id.bookImage);
        }
    }
}
