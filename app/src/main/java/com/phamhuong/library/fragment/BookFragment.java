package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

public class BookFragment extends Fragment {
    TextView txtTitle;
    TextView txtAuthor;
    ImageView imgBook;
    Book book;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle = view.findViewById(R.id.tvBookName);
        txtAuthor = view.findViewById(R.id.tvBookAuthor);
        imgBook = view.findViewById(R.id.bookImage);


        Bundle bundle = getArguments();
        if (bundle != null) {
            book = (Book) bundle.getSerializable("book");
            if (book != null) {
                txtTitle.setText(book.getTitle());
                txtAuthor.setText(book.getAuthor());
                // Load ảnh từ URL bằng Glide
                Glide.with(this)
                        .load(book.getCoverUrl())
                        .into(imgBook);
            }
        }
    }
}
