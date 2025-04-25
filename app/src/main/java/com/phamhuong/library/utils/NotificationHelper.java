package com.phamhuong.library.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.model.CreateNotificationRequest;
import com.phamhuong.library.model.Notification;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHelper { // Đặt tên class cho phù hợp
    private Context context;
    private APIService apiService; // Sử dụng cùng một APIService để gọi các API khác

    public NotificationHelper(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getRetrofit().create(APIService.class);
    }

    public void createNotification(String userId, String title, String message, String type) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        CreateNotificationRequest notificationRequest = new CreateNotificationRequest(userId, title, message, type, timestamp);
        Call<Notification> callNotification = apiService.createNotification(notificationRequest);
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

    public void checkAndSendInitialNotifications(String userId) {
        checkNewBooksNotification(userId);
        checkDueBooksNotification(userId);
    }

    private void checkNewBooksNotification(String userId) {
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        int year = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year = currentDate.getYear();
        }
        int month = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            month = currentDate.getMonthValue();
        }
        Call<List<Book>> callNewBooks = apiService.getNewBooksInMonth(year, month);
        callNewBooks.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String title = "Sách mới trong tháng!";
                    String message = "Có " + response.body().size() + " sách mới ra trong tháng này. Hãy khám phá ngay!";
                    createNotification(userId, title, message, "NEW_BOOK");
                } else {
                    Log.d("NotificationAPI", "Không có sách mới trong tháng này hoặc lỗi khi tải.");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("NotificationAPI", "Lỗi khi kiểm tra sách mới: " + t.getMessage());
            }
        });
    }

    private void checkDueBooksNotification(String userId) {
        Call<List<BorrowingRecord>> callDueSoonBooks = apiService.getDueSoonBooksByUser(Integer.parseInt(userId), 3);
        Log.d("NotificationAPI", "Calling API: " + callDueSoonBooks.request().url());
        callDueSoonBooks.enqueue(new Callback<List<BorrowingRecord>>() {
            @Override
            public void onResponse(Call<List<BorrowingRecord>> call, Response<List<BorrowingRecord>> response) {
                Log.d("NotificationAPI", "User ID: " + userId);
                Log.d("NotificationAPI", "Response: " + response.toString());
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String title = "Sắp đến hạn trả!";
                    StringBuilder messageBuilder = new StringBuilder("Các sách sau sắp đến hạn trả: ");
                    for (BorrowingRecord record : response.body()) {
                        messageBuilder.append("'").append(record.getBook().getTitle()).append("', ");
                    }
                    if (messageBuilder.length() > 0) {
                        messageBuilder.delete(messageBuilder.length() - 2, messageBuilder.length()); // Xóa dấu phẩy và khoảng trắng cuối cùng
                        createNotification(userId, title, messageBuilder.toString(), "DUE_REMINDER");
                    } else {
                        Log.d("NotificationAPI", "Không có sách sắp đến hạn trả.");
                    }
                } else {
                    Log.d("NotificationAPI", "Không có sách sắp đến hạn trả hoặc lỗi khi tải.");
                }
            }

            @Override
            public void onFailure(Call<List<BorrowingRecord>> call, Throwable t) {
                Log.e("NotificationAPI", "Lỗi khi kiểm tra sách sắp đến hạn: " + t.getMessage());
            }
        });
    }
}