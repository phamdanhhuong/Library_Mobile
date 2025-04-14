package com.phamhuong.library.adapter.store;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.phamhuong.library.fragment.recycle.FragmentCategories;
import com.phamhuong.library.fragment.store.AudiobookFragment;
import com.phamhuong.library.fragment.store.BestSellerFragment;
import com.phamhuong.library.fragment.store.FragmentTabEBooks;
import com.phamhuong.library.fragment.store.NewReleasesFragment;
import com.phamhuong.library.fragment.store.PopularFreeFragment;

public class StoreAdapter extends FragmentStateAdapter {

    public StoreAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentTabEBooks();
            case 1:
                return new AudiobookFragment();
            case 2:
                return new FragmentCategories();
            case 3:
                return new BestSellerFragment();
            case 4:
                return new NewReleasesFragment();
            case 5:
                return new PopularFreeFragment();
            default:
                return new FragmentTabEBooks();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}