package com.phamhuong.library.fragment.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class FragmentUserInfo extends Fragment {
    private String fullName;
    private String avatarUrl;
    ImageView imgAvatar;
    TextView tvGreeting;
    TextView tvReadCount; // Thêm TextView này
    TextView tvSavedCount; // Thêm TextView này
    TextView tvBorrowedCount; // Thêm TextView này
    APIService apiService;
    private int userId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Lấy userId khi Fragment được attach
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        if (userLoginInfo != null) {
            userId = userLoginInfo.getUserId();
            fullName = userLoginInfo.getFullName();
            avatarUrl = userLoginInfo.getAvatarUrl();
        } else {
            // Xử lý trường hợp không có thông tin đăng nhập
            Log.e("FragmentUserInfo", "Không tìm thấy thông tin người dùng.");
            // Có thể chuyển người dùng về trang đăng nhập hoặc hiển thị thông báo lỗi
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvGreeting = view.findViewById(R.id.tv_greeting);
        tvReadCount = view.findViewById(R.id.tv_read_count);
        tvSavedCount = view.findViewById(R.id.tv_saved_count);
        tvBorrowedCount = view.findViewById(R.id.tv_borrowed_count);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fullName != null) {
            tvGreeting.setText("Xin chào, " + fullName + "!");
        }
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar1)
                    .error(R.drawable.avatar1)
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.avatar1);
        }

        // Gọi các API để lấy số lượng
        if (userId != 0) {
            getReservationCount(userId);
            getBorrowingCount(userId);
            getWishlistCount(userId);
        }
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        if (tvGreeting != null) {
            tvGreeting.setText("Xin chào, " + fullName + "!");
        }
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        if (imgAvatar != null) {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.bg_button_read)
                        .error(R.drawable.bg_button_read)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.bg_button_read);
            }
        }
    }

    private void getReservationCount(int userId) {
        Call<ApiResponseT<List<Reservation>>> call = apiService.getReservationHistoryByUserId(userId);
        call.enqueue(new Callback<ApiResponseT<List<Reservation>>>() {
            @Override
            public void onResponse(Call<ApiResponseT<List<Reservation>>> call, Response<ApiResponseT<List<Reservation>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    tvReadCount.setText(String.valueOf(response.body().getData().size()));
                } else {
                    tvReadCount.setText("0"); // Hoặc xử lý lỗi khác
                    Log.e("FragmentUserInfo", "Lỗi khi lấy số lượng đặt sách: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseT<List<Reservation>>> call, Throwable t) {
                tvReadCount.setText("0"); // Hoặc xử lý lỗi khác
                Log.e("FragmentUserInfo", "Lỗi kết nối khi lấy số lượng đặt sách: " + t.getMessage());
            }
        });
    }

    private void getBorrowingCount(int userId) {
        Call<List<BorrowingRecord>> call = apiService.getBorrowingRecordsByUser(userId);
        call.enqueue(new Callback<List<BorrowingRecord>>() {
            @Override
            public void onResponse(Call<List<BorrowingRecord>> call, Response<List<BorrowingRecord>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvBorrowedCount.setText(String.valueOf(response.body().size()));
                } else {
                    tvBorrowedCount.setText("0"); // Hoặc xử lý lỗi khác
                    Log.e("FragmentUserInfo", "Lỗi khi lấy số lượng sách đã mượn: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BorrowingRecord>> call, Throwable t) {
                tvBorrowedCount.setText("0"); // Hoặc xử lý lỗi khác
                Log.e("FragmentUserInfo", "Lỗi kết nối khi lấy số lượng sách đã mượn: " + t.getMessage());
            }
        });
    }

    private void getWishlistCount(int userId) {
        Call<ApiResponseT<List<Book>>> call = apiService.getWishListByUserId(userId);
        call.enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @Override
            public void onResponse(Call<ApiResponseT<List<Book>>> call, Response<ApiResponseT<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    tvSavedCount.setText(String.valueOf(response.body().getData().size()));
                } else {
                    tvSavedCount.setText("0"); // Hoặc xử lý lỗi khác
                    Log.e("FragmentUserInfo", "Lỗi khi lấy số lượng sách đã lưu: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseT<List<Book>>> call, Throwable t) {
                tvSavedCount.setText("0"); // Hoặc xử lý lỗi khác
                Log.e("FragmentUserInfo", "Lỗi kết nối khi lấy số lượng sách đã lưu: " + t.getMessage());
            }
        });
    }
}