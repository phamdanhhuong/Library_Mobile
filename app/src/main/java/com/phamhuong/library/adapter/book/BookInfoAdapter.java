package com.phamhuong.library.adapter.book;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

import java.util.List;

public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.MyViewHolder>{

    private Context context;
    private List<Book> ListBook;

    public BookInfoAdapter(Context context, List<Book> ListBook) {
        this.context = context;
        this.ListBook = ListBook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_book, parent, false);
        return new BookInfoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = ListBook.get(position);
        holder.txtName.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(book.getCoverUrl())
                .into(holder.imgCategory);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookFragment(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListBook.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtAuthor;
        ImageView imgCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tvBookName);
            txtAuthor = itemView.findViewById(R.id.tvBookAuthor);
            imgCategory = itemView.findViewById(R.id.bookImage);
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

        // Thêm animation fade
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        transaction.replace(R.id.content_frame, bookFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
