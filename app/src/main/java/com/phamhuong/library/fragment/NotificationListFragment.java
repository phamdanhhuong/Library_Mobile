package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.NotificationAdapter;
import com.phamhuong.library.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationListFragment extends Fragment {
    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private int categoryType;

    public static NotificationListFragment newInstance(int categoryType) {
        NotificationListFragment fragment = new NotificationListFragment();
        Bundle args = new Bundle();
        args.putInt("categoryType", categoryType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);

        categoryType = getArguments().getInt("categoryType", 0);

        rvNotifications = view.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách thông báo phù hợp với từng danh mục
        notificationList = getNotificationsByCategory(categoryType);

        notificationAdapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(notificationAdapter);

        return view;
    }

    private List<Notification> getNotificationsByCategory(int category) {
        List<Notification> notifications = new ArrayList<>();
        switch (category) {
            case 0: // All
                notifications.add(new Notification("Khuyến mãi", "Giảm giá 50% cho sách mới!", "10 phút trước"));
                notifications.add(new Notification("Đã đặt trước", "Bạn đã đặt sách 'Dune'.", "1 giờ trước"));
                notifications.add(new Notification("Có sẵn", "Sách 'Harry Potter' đã có sẵn.", "Hôm qua"));
                break;
            case 1: // Reserved
                notifications.add(new Notification("Đã đặt trước", "Bạn đã đặt sách 'Dune'.", "1 giờ trước"));
                break;
            case 2: // Available
                notifications.add(new Notification("Có sẵn", "Sách 'Harry Potter' đã có sẵn.", "Hôm qua"));
                break;
        }
        return notifications;
    }
}
