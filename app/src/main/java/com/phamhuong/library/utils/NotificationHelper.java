package com.phamhuong.library.utils;

import android.content.Context;
import android.util.Log;

import com.phamhuong.library.model.CreateNotificationRequest;
import com.phamhuong.library.model.Notification;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;

public class NotificationHelper { // Đặt tên class cho phù hợp
    private Context context;
    private APIService notificationApiService;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationApiService = RetrofitClient.getRetrofit().create(APIService.class);
    }

    public void createNotification(String userId, String title, String message, String type) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        CreateNotificationRequest notificationRequest = new CreateNotificationRequest(userId, title, message, type, timestamp);
        Call<Notification> callNotification = notificationApiService.createNotification(notificationRequest);
        callNotification.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.isSuccessful()) {
                    Log.d("NotificationAPI", "Thông báo đã được tạo thành công.");
                    Notification createdNotification = response.body();
                    // Xử lý đối tượng createdNotification ở đây nếu cần
                    Log.d("NotificationAPI", "ID thông báo đã tạo: " + createdNotification.getId());
                    Log.d("NotificationAPI", "Tiêu đề thông báo: " + createdNotification.getTitle());
                    // ...
                } else {
                    Log.e("NotificationAPI", "Lỗi khi tạo thông báo: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("NotificationAPI", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("NotificationAPI", "Error reading error body", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.e("NotificationAPI", "Lỗi kết nối khi tạo thông báo: " + t.getMessage());
            }
        });
    }
}
