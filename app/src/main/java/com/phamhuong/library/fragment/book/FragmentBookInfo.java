package com.phamhuong.library.fragment.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBookInfo extends Fragment {
    TextView tvBookName, tvBookAuthor, tvBookDescription, tvGenre, tvNumberOfReviews, tvAverageScore;
    ImageView imgBookCover;
    MaterialButton btnAddToWishlist;
    Book book;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_info, container, false);
        initView(view);
        initData(); // Gọi hàm này để set dữ liệu

        btnAddToWishlist.setOnClickListener(v -> addToWishlist());

        return view;
    }

    void addToWishlist () {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int userId = dbHelper.getLoginInfoSQLite().getUserId();
        Map<String,Integer> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("book_id", book.getId());

        apiService.addToWishList(requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(getContext(), "Thêm vào danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm vào danh sách yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("", "Error: " + t.getMessage());
            }
        });
    }
    void initView(View view) {
        tvBookName = view.findViewById(R.id.tvBookName);
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
        tvBookDescription = view.findViewById(R.id.tvDescription);
        imgBookCover = view.findViewById(R.id.bookImage);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvNumberOfReviews = view.findViewById(R.id.tvNumberOfReviews);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);
        btnAddToWishlist = view.findViewById(R.id.btnAddToWishlist);
    }

    void initData() {
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
            tvBookName.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());
            tvBookDescription.setText(book.getSummary());
            String imageUrl = book.getCoverUrl();
            Glide.with(this).load(imageUrl).into(imgBookCover);
        }
    }

    public static FragmentBookInfo newInstance(Book book) {
        FragmentBookInfo fragment = new FragmentBookInfo();
        Bundle args = new Bundle();
        args.putSerializable("book", book);
        fragment.setArguments(args);
        return fragment;
    }
}
