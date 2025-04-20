package com.phamhuong.library.fragment.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookSectionAdapter;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTabEBooks extends Fragment {
    private RecyclerView rvMainContent;
    private BookSectionAdapter sectionAdapter;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_e_books, container, false);
        
        initViews(view);
        setupRecyclerView();
        fetchBooks();

        return view;
    }

    private void initViews(View view) {
        rvMainContent = view.findViewById(R.id.rvMainContent);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMainContent.setLayoutManager(layoutManager);
        
        sectionAdapter = new BookSectionAdapter(getContext());
        rvMainContent.setAdapter(sectionAdapter);
    }

    private void fetchBooks() {
        // Fetch New Releases
        fetchNewReleases();
        
        // Fetch Top Selling
        fetchTopSelling();
        
        // Fetch For You
        fetchForYou();
        
        // Fetch Free Books
        fetchFreeBooks();
    }

    private void fetchNewReleases() {
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("New Releases", response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("Failed to load new releases");
            }
        });
    }

    private void fetchTopSelling() {
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("Top Selling", response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("Failed to load top selling books");
            }
        });
    }

    private void fetchForYou() {
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("For You", response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("Failed to load recommended books");
            }
        });
    }

    private void fetchFreeBooks() {
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("Free Books", response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                showError("Failed to load free books");
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBookClick(Book book) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bookFragment.setArguments(bundle);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, bookFragment)
                .addToBackStack(null)
                .commit();
        }
    }
}