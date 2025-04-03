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
import com.phamhuong.library.adapter.BookAdapterRelate;
import com.phamhuong.library.model.Book;

import java.util.List;

public class FragmentRelateBook extends Fragment {
    private RecyclerView recyclerViewSameAuthor, recyclerViewSameCategory;
    private BookAdapterRelate adapterSameAuthor, adapterSameCategory;
    private List<Book> booksSameAuthor, booksSameCategory;

    public static FragmentRelateBook newInstance(List<Book> sameAuthor, List<Book> sameCategory) {
        FragmentRelateBook fragment = new FragmentRelateBook();
        Bundle args = new Bundle();
        args.putSerializable("booksSameAuthor", (java.io.Serializable) sameAuthor);
        args.putSerializable("booksSameCategory", (java.io.Serializable) sameCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relate_book, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        recyclerViewSameAuthor = view.findViewById(R.id.recyclerViewSameAuthor);
        recyclerViewSameCategory = view.findViewById(R.id.recyclerViewSameCategory);
    }

    private void initData() {
        if (getArguments() != null) {
            // Make sure the data is not null before setting it
            booksSameAuthor = (List<Book>) getArguments().getSerializable("booksSameAuthor");
            booksSameCategory = (List<Book>) getArguments().getSerializable("booksSameCategory");
        }

        // If the data is valid, initialize the adapters and set the RecyclerViews
        if (booksSameAuthor != null) {
            adapterSameAuthor = new BookAdapterRelate(getContext(), booksSameAuthor);
            recyclerViewSameAuthor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewSameAuthor.setAdapter(adapterSameAuthor);
        }

        if (booksSameCategory != null) {
            adapterSameCategory = new BookAdapterRelate(getContext(), booksSameCategory);
            recyclerViewSameCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewSameCategory.setAdapter(adapterSameCategory);
        }
    }
}
