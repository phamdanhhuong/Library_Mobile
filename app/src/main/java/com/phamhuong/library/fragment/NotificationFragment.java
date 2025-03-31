package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.NotificationPagerAdapter;

public class NotificationFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private NotificationPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // Adapter cho ViewPager2
        adapter = new NotificationPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("All");
                    break;
                case 1:
                    tab.setText("Reserved");
                    break;
                case 2:
                    tab.setText("Available");
                    break;
            }
        }).attach();

        return view;
    }
}
