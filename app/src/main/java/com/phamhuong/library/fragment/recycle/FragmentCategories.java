package com.phamhuong.library.fragment.recycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.CategoryAdapter;
import com.phamhuong.library.model.Category;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategories extends Fragment {
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_all_categories, container, false);
        
        rvCategories = view.findViewById(R.id.rvCategories);
        // Set up grid layout with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCategories.setLayoutManager(layoutManager);
        
        // Initialize adapter with categories
        categoryAdapter = new CategoryAdapter(getContext(), getCategories());
        rvCategories.setAdapter(categoryAdapter);
        
        return view;
    }

    private List<Category> getCategories() {
        // Return list of categories
        List<Category> categories = new ArrayList<>();
        // Add categories here
        return categories;
    }
}