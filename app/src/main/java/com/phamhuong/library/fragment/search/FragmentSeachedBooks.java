package com.phamhuong.library.fragment.search;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.fragment.store.FragmentStore;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSeachedBooks extends Fragment {
    private RecyclerView rvFilteredBooks;
    private BookHorizontalAdapter adapter;
    private String searchQuery;
    private TextView tvSearchQuery;
    private ImageButton btnBack;
    private List<Book> bookList;
    private LinearLayout layoutNoSearchResults;
    public static FragmentSeachedBooks newInstance(String query) {
        FragmentSeachedBooks fragment = new FragmentSeachedBooks();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery = getArguments().getString("query", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_books, container, false);

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadFilteredBooks();

        return view;
    }

    private void initViews(View view) {
        rvFilteredBooks = view.findViewById(R.id.rvFilteredBooks);
        tvSearchQuery = view.findViewById(R.id.tvSearchQuery);
        btnBack = view.findViewById(R.id.btnBack);

        tvSearchQuery.setText("Kết quả tìm kiếm cho \"" + searchQuery + "\"");
        layoutNoSearchResults = view.findViewById(R.id.layoutNoSearchResults);
    }

    private void setupRecyclerView() {
        bookList = new ArrayList<>();
        adapter = new BookHorizontalAdapter(getContext(), bookList);
        rvFilteredBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFilteredBooks.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new FragmentStore())
                        .commit();
            }
        });
    }

    private void loadFilteredBooks() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.searchBooks(searchQuery).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList.clear();
                    bookList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (bookList.isEmpty()) {
                        layoutNoSearchResults.setVisibility(View.VISIBLE);
                        rvFilteredBooks.setVisibility(View.GONE);
                    }
                    else {
                        layoutNoSearchResults.setVisibility(View.GONE);
                        rvFilteredBooks.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    rvFilteredBooks.setVisibility(View.GONE);
                    layoutNoSearchResults.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                rvFilteredBooks.setVisibility(View.GONE);
                layoutNoSearchResults.setVisibility(View.VISIBLE);
            }
        });
    }
}