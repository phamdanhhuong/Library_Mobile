package com.phamhuong.library.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.service.APIService;

import java.util.List;

public class FragmentBrownTo extends Fragment {
    APIService apiService;
    private RecyclerView rvTrendingBooks;
    private List<Book> trendingBooks;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brown_to, container, false);

        return view;
    }
}
