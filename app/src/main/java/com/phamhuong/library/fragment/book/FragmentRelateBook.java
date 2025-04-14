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
    String token;
    TextView tvByAuthor;
    private RecyclerView recyclerViewSameAuthor, recyclerViewSameCategory;
    private BookVerticalAdapter adapterSameAuthor, adapterSameCategory;
    private List<Book> booksSameAuthor= new ArrayList<>(), booksSameCategory = new ArrayList<>();
    ;

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

        if (booksSameAuthor != null) {
            adapterSameAuthor = new BookVerticalAdapter(getContext(), booksSameAuthor);
            recyclerViewSameAuthor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewSameAuthor.setAdapter(adapterSameAuthor);
        }

        if (booksSameCategory != null) {
            adapterSameCategory = new BookVerticalAdapter(getContext(), booksSameCategory);
            recyclerViewSameCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewSameCategory.setAdapter(adapterSameCategory);
        }
        return view;
    }

    private void initView(View view) {
        tvByAuthor = view.findViewById(R.id.tvByAuthor);
        recyclerViewSameAuthor = view.findViewById(R.id.recyclerViewSameAuthor);
        recyclerViewSameCategory = view.findViewById(R.id.recyclerViewSameCategory);
    }

    private void initData() {
        getBooksByCategory(getArguments().getString("sameCategory"));
        getBooksByAuthor(getArguments().getString("sameAuthor"));

    }
    private void getBooksByAuthor(String author) {
        tvByAuthor.setText("Bởi " + author);
        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        apiService.getBooksByAuthor(author).enqueue(new Callback<List<Book>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    booksSameAuthor.clear();
                    booksSameAuthor.addAll(response.body());
                    adapterSameAuthor.notifyDataSetChanged();
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
        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        apiService.getBookByCategory(category).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    booksSameCategory.clear();
                    booksSameCategory.addAll(response.body());
                    adapterSameCategory.notifyDataSetChanged();
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
