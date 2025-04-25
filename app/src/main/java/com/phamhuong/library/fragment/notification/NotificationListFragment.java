package com.phamhuong.library.fragment.notification;

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
import com.phamhuong.library.adapter.nofitication.NotificationAdapter;
import com.phamhuong.library.model.Notification;
import com.phamhuong.library.model.NotificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationListFragment extends Fragment implements NotificationAdapter.OnNotificationClickListener {
    private static final String ARG_TAB_POSITION = "tab_position";
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> allNotifications; // Giữ toàn bộ danh sách thông báo
    private List<Notification> displayedNotifications; // Danh sách thông báo sẽ hiển thị

    public static NotificationListFragment newInstance(int tabPosition) {
        NotificationListFragment fragment = new NotificationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);

        recyclerView = view.findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allNotifications = getNotifications();
        displayedNotifications = new ArrayList<>(allNotifications); // Ban đầu hiển thị tất cả

        // Lọc danh sách hiển thị dựa trên vị trí tab
        if (getArguments() != null) {
            int tabPosition = getArguments().getInt(ARG_TAB_POSITION);
            filterNotifications(tabPosition);
        }

        adapter = new NotificationAdapter(displayedNotifications, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void filterNotifications(int tabPosition) {
        displayedNotifications.clear();
        switch (tabPosition) {
            case 0: // All
                displayedNotifications.addAll(allNotifications);
                break;
            case 1: // Reserved (Ví dụ: Lọc theo trạng thái hoặc loại thông báo liên quan đến đặt trước)
                displayedNotifications.addAll(allNotifications.stream()
                        .filter(notification -> notification.getType() == NotificationType.DUE_REMINDER) // Ví dụ: Thông báo sắp đến hạn trả có thể liên quan đến đặt trước
                        .collect(Collectors.toList()));
                break;
            case 2: // Available (Ví dụ: Lọc theo loại thông báo về sách mới)
                displayedNotifications.addAll(allNotifications.stream()
                        .filter(notification -> notification.getType() == NotificationType.NEW_BOOK || notification.getType() == NotificationType.PROMOTION || notification.getType() == NotificationType.RECOMMENDATION)
                        .collect(Collectors.toList()));
                break;
            default:
                displayedNotifications.addAll(allNotifications);
                break;
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();


        return notifications;
    }

    @Override
    public void onNotificationClick(Notification notification) {
        // Mark notification as read
        notification.setRead(true);
        adapter.notifyDataSetChanged();

        // Handle notification click based on type
        switch (notification.getType()) {
            case NEW_BOOK:
                // Navigate to book details
                navigateToBookDetails(notification);
                break;
            case DUE_REMINDER:
                // Navigate to borrowed books
                navigateToBorrowedBooks();
                break;
            case PROMOTION:
                // Navigate to promotions page
                navigateToPromotions();
                break;
            case RECOMMENDATION:
                // Navigate to recommended book
                navigateToBookDetails(notification);
                break;
            case ORDER_STATUS:
                // Navigate to order details
                navigateToOrderDetails(notification);
                break;
            case SYSTEM:
                // Show system message dialog
                showSystemMessageDialog(notification);
                break;
        }
    }

    private void navigateToBookDetails(Notification notification) {
        // TODO: Implement navigation to book details
    }

    private void navigateToBorrowedBooks() {
        // TODO: Implement navigation to borrowed books
    }

    private void navigateToPromotions() {
        // TODO: Implement navigation to promotions page
    }

    private void navigateToOrderDetails(Notification notification) {
        // TODO: Implement navigation to order details
    }

    private void showSystemMessageDialog(Notification notification) {
        // TODO: Implement system message dialog
    }
    public void addNotification(Notification notification) {
        allNotifications.add(0, notification); // Thêm lên đầu danh sách
        if (getArguments() != null) {
            int tabPosition = getArguments().getInt(ARG_TAB_POSITION);
            filterNotifications(tabPosition);
        }
    }

}