package com.phamhuong.library.fragment.book;

import android.os.Bundle;
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
import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

public class FragmentBookInfo extends Fragment {
    TextView tvBookName, tvBookAuthor, tvBookDescription, tvGenre, tvNumberOfReviews, tvAverageScore;
    ImageView imgBookCover;
    ImageButton btnAddToWishlist;
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
        Toast.makeText(getParentFragment().getContext(), "Bạn đã bấm vào wish list", Toast.LENGTH_SHORT).show();
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
