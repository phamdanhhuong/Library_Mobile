package com.phamhuong.library.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.adapter.recycle.BookVerticalAdapter;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTrending extends Fragment implements BookHorizontalAdapter.OnBookClickListener {
    private APIService apiService;
    private RecyclerView rvTrendingBooks;
    private BookVerticalAdapter bookAdapter;
    private List<Book> trendingBooks;
    private String token ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        initViews(view);
        setupRecyclerView();
        fetchAllBooks();

        return view;
    }

    private void initViews(View view) {
        rvTrendingBooks = view.findViewById(R.id.recyclerViewTrending);
        trendingBooks = new ArrayList<>();
    }

    private void setupRecyclerView() {
        bookAdapter = new BookVerticalAdapter(getContext(), trendingBooks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTrendingBooks.setLayoutManager(layoutManager);
        rvTrendingBooks.setAdapter(bookAdapter);
    }

    private void fetchAllBooks() {
        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trendingBooks.clear();
                    trendingBooks.addAll(response.body());
                    bookAdapter.notifyDataSetChanged();
                } else {
                    showError("Không thể tải danh sách sách");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBookClick(Book book) {
        // Handle book click by opening BookFragment
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bookFragment.setArguments(bundle);

        // Replace current fragment with BookFragment
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, bookFragment)
                .addToBackStack(null)
                .commit();
        }
    }
}