package com.phamhuong.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.adapter.GenreAdapter;

import java.util.Arrays;
import java.util.List;

public class SearchInputFragment extends Fragment {
    private RecyclerView rvGenres;
    private GenreAdapter genreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_input, container, false);

        rvGenres = view.findViewById(R.id.rvGenres);
        rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<String> genres = Arrays.asList("Fantasy", "Mystery", "Sci-Fi", "Romance", "Horror", "Non-Fiction");
        genreAdapter = new GenreAdapter(genres);
        rvGenres.setAdapter(genreAdapter);

        return view;
    }
}
