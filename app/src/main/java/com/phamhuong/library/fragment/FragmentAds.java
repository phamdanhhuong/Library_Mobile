package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.AdsPagerAdapter;

import java.util.Arrays;
import java.util.List;
import android.os.Handler;
import android.os.Looper;

public class FragmentAds extends Fragment {
    private ViewPager2 viewPagerAds;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentIndex = 0;
    private AdsPagerAdapter adapter;
    private List<Integer> imageList = Arrays.asList(
            R.drawable.img_ad_1, R.drawable.img_ad_2
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads, container, false);
        viewPagerAds = view.findViewById(R.id.viewPagerAds);

        adapter = new AdsPagerAdapter(imageList);
        viewPagerAds.setAdapter(adapter);

        // Auto-scroll every 3 seconds
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex == imageList.size() - 1) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                viewPagerAds.setCurrentItem(currentIndex, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }
}
