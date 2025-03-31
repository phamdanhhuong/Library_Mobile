package com.phamhuong.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Load các fragment con vào container
        loadFragment(new SearchInputFragment(), R.id.fragmentSearchInput);
        loadFragment(new TrendingBooksFragment(), R.id.fragmentTrendingBooks);
        loadFragment(new NewBooksFragment(), R.id.fragmentNewBooks);
        loadFragment(new SearchResultsFragment(), R.id.fragmentSearchResults);

        return view;
    }
    private void loadFragment(Fragment fragment, int containerId) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment);
        ft.commit();
    }
}

