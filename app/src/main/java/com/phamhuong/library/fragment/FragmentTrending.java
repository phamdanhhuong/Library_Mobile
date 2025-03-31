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

public class FragmentTrending extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrending);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        bookList = new ArrayList<>();
        loadTrendingBooks();

        bookAdapter = new BookAdapter(this.getContext(),bookList);
        recyclerView.setAdapter(bookAdapter);

        return view;
    }

    private void loadTrendingBooks() {
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
        bookList.add(new Book());
    }
}

