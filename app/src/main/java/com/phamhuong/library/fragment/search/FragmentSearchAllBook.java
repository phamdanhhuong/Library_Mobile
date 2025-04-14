package com.phamhuong.library.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookSectionAdapter;

public class FragmentSearchAllBook extends Fragment {
    private RecyclerView rvMainContent;
    private BookSectionAdapter sectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_all_book, container, false);
        
        rvMainContent = view.findViewById(R.id.rvMainContent);
        rvMainContent.setLayoutManager(new LinearLayoutManager(getContext()));
        
        sectionAdapter = new BookSectionAdapter(getContext());
        rvMainContent.setAdapter(sectionAdapter);
        
        return view;
    }
}