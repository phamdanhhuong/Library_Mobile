package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;

public class FragmentCommentSection extends Fragment {
    private int bookId;
    private TextView tvAverageRating, tvNumberOfComments;
    private RatingBar ratingBarAverage, ratingBarUser;
    private RecyclerView recyclerViewComments;
    private EditText edtComment;
    private ImageView btnSendComment;

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
        // TODO: Fetch comments from database or API using bookId
        // Cập nhật UI dựa trên dữ liệu lấy được
    }
}

