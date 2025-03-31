package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.BookAdapter;
import com.phamhuong.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class TrendingBooksFragment extends Fragment {
    private RecyclerView rvTrendingBooks;
    private BookAdapter bookAdapter;
    private List<Book> trendingBooks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_books, container, false);

        // Ánh xạ RecyclerView
        rvTrendingBooks = view.findViewById(R.id.rvTrendingBooks);
        rvTrendingBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Tạo danh sách sách thịnh hành
        trendingBooks = new ArrayList<>();
        trendingBooks.add(new Book());
        trendingBooks.add(new Book());
        trendingBooks.add(new Book());

        // Gán adapter cho RecyclerView
        bookAdapter = new BookAdapter(this.getContext(),trendingBooks);
        rvTrendingBooks.setAdapter(bookAdapter);

        return view;
    }
}
