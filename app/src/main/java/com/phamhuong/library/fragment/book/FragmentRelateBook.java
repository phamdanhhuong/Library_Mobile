package com.phamhuong.library.fragment.book;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.home.BookAdapterRelate;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.adapter.recycle.BookSectionAdapter;
import com.phamhuong.library.adapter.recycle.BookVerticalAdapter;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRelateBook extends Fragment {
    private APIService apiService;
    String token = "";
    TextView tvByAuthor;
    private RecyclerView recyclerViewRelate;
    private BookSectionAdapter sectionAdapter;

    public static FragmentRelateBook newInstance(String sameAuthor, String sameCategory) {
        FragmentRelateBook fragment = new FragmentRelateBook();
        Bundle args = new Bundle();
        args.putSerializable("sameAuthor", (java.io.Serializable) sameAuthor);
        args.putSerializable("sameCategory", (java.io.Serializable) sameCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relate_book, container, false);
        initView(view);
        initData();
        setupRecyclerView();
        return view;
    }

    private void initView(View view) {
        recyclerViewRelate = view.findViewById(R.id.recyclerViewRelate);
    }

    private void initData() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        getBooksByCategory(getArguments().getString("sameCategory"));
        getBooksByAuthor(getArguments().getString("sameAuthor"));

    }
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRelate.setLayoutManager(layoutManager);

        sectionAdapter = new BookSectionAdapter(getContext());
        recyclerViewRelate.setAdapter(sectionAdapter);
    }
    private void getBooksByAuthor(String author) {
        apiService.getBooksByAuthor(author).enqueue(new Callback<List<Book>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("Bởi " + author, response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBooksByCategory(String category) {
        apiService.getBookByCategory(category).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.addSection("Sách tương tự", response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
