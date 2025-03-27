package com.phamhuong.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.model.Book;

public class BookFragment extends Fragment {
    TextView txtTitle;
    Book book;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle = view.findViewById(R.id.tvBookTile);
        Bundle bundle = getArguments();
        if (bundle != null) {
            book = (Book) bundle.getSerializable("book");

            if (book != null) {
                txtTitle.setText(book.getTitle());
            }
        }
    }
}
