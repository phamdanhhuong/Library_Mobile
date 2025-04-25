package com.phamhuong.library.fragment.notification;

import android.os.Bundle;
import android.util.Log;
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
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends Fragment implements NotificationAdapter.OnNotificationClickListener {
    private static final String ARG_TAB_POSITION = "tab_position";
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> allNotifications; // Giữ toàn bộ danh sách thông báo
    private List<Notification> displayedNotifications;
    private APIService apiService; // Danh sách thông báo sẽ hiển thị

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

        allNotifications = new ArrayList<>(); // Khởi tạo rỗng
        displayedNotifications = new ArrayList<>(allNotifications); // Ban đầu hiển thị tất cả
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        // Lọc danh sách hiển thị dựa trên vị trí tab
        if (getArguments() != null) {
            int tabPosition = getArguments().getInt(ARG_TAB_POSITION);
            fetchNotifications(tabPosition);
        }

        adapter = new NotificationAdapter(displayedNotifications, this);
        recyclerView.setAdapter(adapter);

        return view;
    }
    private void fetchNotifications(final int tabPosition) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        long userId = dbHelper.getLoginInfoSQLite().getUserId(); // Lấy userId kiểu long

        Call<List<Notification>> call = apiService.getNotificationsByUser(userId);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    allNotifications = response.body(); // Lấy danh sách thông báo từ response
                    filterNotifications(tabPosition); // Lọc và hiển thị
                    adapter.updateNotifications(displayedNotifications); // Cập nhật adapter
                } else {
                    Log.e("NotificationListFragment", "Failed to fetch notifications: " + response.code());
                    // Xử lý lỗi (ví dụ: hiển thị thông báo cho người dùng)
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e("NotificationListFragment", "Error fetching notifications: " + t.getMessage());
                // Xử lý lỗi kết nối
            }
        });
    }
    private void filterNotifications(int tabPosition) {
        displayedNotifications.clear();
        if (allNotifications != null) { // Kiểm tra allNotifications khác null
            switch (tabPosition) {
                case 0: // All
                    displayedNotifications.addAll(allNotifications);
                    break;
                case 1: // Reserved (Ví dụ: Lọc theo trạng thái hoặc loại thông báo liên quan đến đặt trước)
                    displayedNotifications.addAll(allNotifications.stream()
                            .filter(notification -> notification.getType().equals(NotificationType.DUE_REMINDER.name()))
                            .collect(Collectors.toList()));
                    break;
                case 2: // Available (Ví dụ: Lọc theo loại thông báo về sách mới)
                    displayedNotifications.addAll(allNotifications.stream()
                            .filter(notification -> notification.getType().equals(NotificationType.NEW_BOOK.name()) || notification.getType().equals(NotificationType.PROMOTION.name()) || notification.getType().equals(NotificationType.RECOMMENDATION.name()))
                            .collect(Collectors.toList()));
                    break;
                default:
                    displayedNotifications.addAll(allNotifications);
                    break;
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
        allNotifications.add(0, notification);
        if (getArguments() != null) {
            int tabPosition = getArguments().getInt(ARG_TAB_POSITION);
            filterNotifications(tabPosition);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}