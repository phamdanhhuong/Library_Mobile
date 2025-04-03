package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;

public class FragmentBookInfo extends Fragment {
    TextView tvBookName, tvBookAuthor, tvBookDescription;
    ImageView imgBookCover;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_info, container, false);
        initView(view);
        initData(); // Gọi hàm này để set dữ liệu
        return view;
    }

    void initView(View view) {
        tvBookName = view.findViewById(R.id.tvBookName);
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
        tvBookDescription = view.findViewById(R.id.tvDescription);
        imgBookCover = view.findViewById(R.id.bookImage);
    }

    void initData() {
        if (getArguments() != null) {
            tvBookName.setText(getArguments().getString("name", "Unknown Title"));
            tvBookAuthor.setText(getArguments().getString("author", "Unknown Author"));
            tvBookDescription.setText(getArguments().getString("description", "No description available"));
            String imageUrl = getArguments().getString("imageUrl", "");
            Glide.with(this).load(imageUrl).into(imgBookCover);
        }
    }

    public static FragmentBookInfo newInstance(String name, String author,String description, String imageUrl) {
        FragmentBookInfo fragment = new FragmentBookInfo();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("author", author);
        args.putString("description", description);
        args.putString("imageUrl", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }
}
