package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.ReviewAdapter;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.Review;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCommentSection extends Fragment {
    private int bookId;
    private APIService reviewApiService;
    private TextView tvAverageRating, tvNumberOfComments;
    private RatingBar ratingBarAverage, ratingBarUser;
    private RecyclerView recyclerViewComments;
    private EditText edtComment;
    private ImageView btnSendComment;
    String token;

    public static FragmentCommentSection newInstance(int bookId) {
        FragmentCommentSection fragment = new FragmentCommentSection();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_section, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        tvAverageRating = view.findViewById(R.id.tvAverageRating);
        tvNumberOfComments = view.findViewById(R.id.tvNumberOfComments);
        ratingBarAverage = view.findViewById(R.id.ratingBarAverage);
        ratingBarUser = view.findViewById(R.id.ratingBarUser);
        recyclerViewComments = view.findViewById(R.id.recyclerViewComments);
        edtComment = view.findViewById(R.id.edtComment);
        btnSendComment = view.findViewById(R.id.btnSendComment);
    }

    private void initData() {
        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId", -1);
            if (bookId != -1) {
                loadComments(bookId);
            }
        }
    }

    private void loadComments(int bookId) {
        reviewApiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        reviewApiService.getReviewsByBook(bookId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviews = response.body();

                    // Cập nhật số lượng bình luận
                    tvNumberOfComments.setText(String.format("(%d đánh giá)", reviews.size()));

                    // Tính trung bình số sao
                    float totalRating = 0;
                    for (Review review : reviews) {
                        totalRating += review.getRating();
                    }
                    float averageRating = reviews.isEmpty() ? 0 : totalRating / reviews.size();

                    // Hiển thị trung bình đánh giá
                    ratingBarAverage.setRating(averageRating);
                    tvAverageRating.setText(String.format("★ %.1f", averageRating));

                    // Hiển thị danh sách đánh giá
                    ReviewAdapter commentAdapter = new ReviewAdapter(getContext(), reviews);
                    recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerViewComments.setAdapter(commentAdapter);
                } else {
                    Toast.makeText(getContext(), "Không thể tải đánh giá", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

