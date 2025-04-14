package com.phamhuong.library.adapter.nofitication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.phamhuong.library.fragment.notification.NotificationListFragment;

public class NotificationPagerAdapter extends FragmentStateAdapter {
    public NotificationPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NotificationListFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab (All, Reserved, Available)
    }
}
