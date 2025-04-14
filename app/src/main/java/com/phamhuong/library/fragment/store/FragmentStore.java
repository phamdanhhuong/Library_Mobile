package com.phamhuong.library.fragment.store;

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
import com.phamhuong.library.adapter.store.StoreAdapter;

public class FragmentStore extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StoreAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        
        initViews(view);
        setupViewPager();
        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
}

    private void setupViewPager() {
        pagerAdapter = new StoreAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Link TabLayout with ViewPager2
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Sách điện tử");
                        break;
                    case 1:
                        tab.setText("Sách nói");
                        break;
                    case 2:
                        tab.setText("Thể loại");
                        break;
                    case 3:
                        tab.setText("Bán chạy nhất");
                        break;
                    case 4:
                        tab.setText("Mới phát hành");
                        break;
                    case 5:
                        tab.setText("Miễn phí phổ biến");
                        break;
                }
            }
        );
        mediator.attach();
    }
}