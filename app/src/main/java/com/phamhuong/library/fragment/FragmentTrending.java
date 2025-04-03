package com.phamhuong.library.fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.phamhuong.library.adapter.BookAdapterTrending;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTrending extends Fragment {
    APIService apiService;
    private RecyclerView rvTrendingBooks;
    private BookAdapterTrending bookAdapter;
    private List<Book> trendingBooks;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        // Ánh xạ RecyclerView
        rvTrendingBooks = view.findViewById(R.id.recyclerViewTrending);

        fetchAllBook();
        return view;
    }
    public void fetchAllBook(){
        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()){
                    trendingBooks = response.body();

                    // Gán adapter cho RecyclerView
                    bookAdapter = new BookAdapterTrending(getContext(),trendingBooks);
                    rvTrendingBooks.setAdapter(bookAdapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rvTrendingBooks.setLayoutManager(layoutManager);
                    rvTrendingBooks.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

