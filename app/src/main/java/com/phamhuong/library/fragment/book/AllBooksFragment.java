package com.phamhuong.library.fragment.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookVerticalAdapter;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class AllBooksFragment extends Fragment {
    private String sectionTitle;
    private List<Book> books;
    private RecyclerView rvBooks;
    private BookVerticalAdapter adapter;

    public static AllBooksFragment newInstance(String title, ArrayList<Book> books) {
        AllBooksFragment fragment = new AllBooksFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("books", books);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionTitle = getArguments().getString("title");
            books = (List<Book>) getArguments().getSerializable("books");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_books, container, false);
        
        initViews(view);
        setupRecyclerView();
        
        return view;
    }

    private void initViews(View view) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(sectionTitle);

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        rvBooks = view.findViewById(R.id.rvBooks);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvBooks.setLayoutManager(layoutManager);

        adapter = new BookVerticalAdapter(getContext(), books);

        rvBooks.setAdapter(adapter);
    }
}