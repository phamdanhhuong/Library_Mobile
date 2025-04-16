package com.phamhuong.library.fragment.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFreeFragment extends FilteredBooksFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadBooks() {
        // Load best seller books from API
        APIService apiService = RetrofitClient.getRetrofit("").create(APIService.class);
        apiService.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBooks = response.body();

                    applyFilters();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Handle error
            }
        });
    }

    @Override
    protected void setupFilters() {
        isFreeOnly = true;
    }
    @Override
    protected void setupView() {
        btnPrice.setVisibility(View.GONE);
    }

}