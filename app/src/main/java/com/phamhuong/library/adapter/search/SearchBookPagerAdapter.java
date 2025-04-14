package com.phamhuong.library.adapter.search;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class SearchBookPagerAdapter extends FragmentStateAdapter {

    public SearchBookPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            //case 0: return new SachDienTuFragment();
            //case 1: return new TheLoaiFragment();
            //case 2: return new ThinhHanhFragment();
            // 3: return new MoiPhatHanhFragment();
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}