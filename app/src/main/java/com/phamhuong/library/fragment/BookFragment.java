package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.ReviewAdapter;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.Review;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {
    private Book book;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        // Nhận dữ liệu từ Bundle
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        if (view.getLayoutParams() != null) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        // Load Fragment con
        loadFragment(FragmentBookInfo.newInstance(book.getTitle(), book.getAuthor(), book.getSummary(), book.getCoverUrl()), R.id.fragmentBookInfo);
        loadFragment(FragmentCommentSection.newInstance(book.getId()), R.id.fragmentCommentSection);
        loadFragment(FragmentRelateBook.newInstance(book.getAuthor(), book.getGenre()), R.id.fragmentRelateBook);

        return view;
    }

    private void loadFragment(Fragment fragment, int containerId) {
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
}
