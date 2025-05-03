package com.phamhuong.library.fragment.reservation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.reservation.BorrowHistoryAdapter;
import com.phamhuong.library.adapter.reservation.ReservationHistoryAdapter;
import com.phamhuong.library.model.ApiReponseWithNoData;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.model.NotificationType;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;
import com.phamhuong.library.utils.CustomDialogHelper;
import com.phamhuong.library.utils.DateFormatter;
import com.phamhuong.library.utils.NotificationHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BorrowHistoryFragment extends Fragment implements BorrowHistoryAdapter.OnRenewButtonClickListener {
    private RecyclerView rvHistory;
    private BorrowHistoryAdapter adapter;
    private List<BorrowingRecord> histories;
    private ProgressBar progressBar;
    private View emptyView;
    private View loadingView;
    private View errorView;
    private View layoutNoHistory;
    private ViewGroup contentView;
    private ShimmerFrameLayout shimmerLayout;
    private APIService apiService;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow_history, container, false);

        initViews(view);
        setupRecyclerView();
        loadBorrowHistory();

        return view;
    }

    private void initViews(View view) {
        rvHistory = view.findViewById(R.id.recyclerView);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        errorView = view.findViewById(R.id.errorState);
        contentView = view.findViewById(R.id.contentLayout);

        layoutNoHistory = view.findViewById(R.id.layoutNoHistory);

        Button btnRetry = errorView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> loadBorrowHistory());
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        showLoading();
    }

    private void setupRecyclerView() {
        histories = new ArrayList<>();
        adapter = new BorrowHistoryAdapter(getContext(), new ArrayList<>(), this);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(adapter);
    }
    private void showLoading() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        errorView.setVisibility(View.GONE);
        layoutNoHistory.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }

    private void showEmpty() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        layoutNoHistory.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void showContent() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        layoutNoHistory.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }
    private void loadBorrowHistory() {
        showLoading();

        if (getContext() == null) return;
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        int userId = userLoginInfo.getUserId();

        Log.d("BorrowHistoryFragment", "userId: " + userId);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getBorrowingRecordsByUser(userId).enqueue(new Callback<List<BorrowingRecord>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<BorrowingRecord>> call, @NonNull Response<List<BorrowingRecord>> response) {
                Log.d("BorrowHistoryFragment", "Response received");

                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowingRecord> records = response.body();
                    Log.d("BorrowHistoryFragment", "Records received: " + records.size());

                    histories.clear();
                    histories.addAll(records);
                    adapter.updateData(records);
                    adapter.notifyDataSetChanged();
                    if (histories.isEmpty()) {
                        showEmpty();
                    } else {
                        showContent();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BorrowingRecord>> call, Throwable t) {
                Log.e("BorrowHistoryFragment", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onRenewButtonClick(BorrowingRecord record) {
        if (getContext() != null && record.getRecordId() != null) {
            Calendar calendar = Calendar.getInstance();
            LocalDate dueDate;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dueDate = LocalDate.parse(record.getDueDate(), DateFormatter.getApiDateTimeFormatter());
            } else {
                dueDate = null;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calendar.set(dueDate.getYear(), dueDate.getMonthValue() - 1, dueDate.getDayOfMonth());
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate selectedDate = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.d("BorrowHistoryFragment", "Renew date: " + selectedDate.format(DateTimeFormatter.ISO_DATE) + " recordId: " + record.getRecordId());
                            renewBook(record.getRecordId(), selectedDate, record.getBook().getTitle());
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        } else if (getContext() != null) {
            Toast.makeText(getContext(), "Không thể gia hạn", Toast.LENGTH_SHORT).show();
        }
    }

    private void renewBook(int recordId, LocalDate renewalDate, String bookTitle) {
        Log.d("BorrowHistoryFragment", "Renew request: recordId=" + recordId + ", selectedDate=" + renewalDate);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.renewBorrowingRecord(recordId, renewalDate).enqueue(new Callback<ApiReponseWithNoData>() {
            @Override
            public void onResponse(@NonNull Call<ApiReponseWithNoData> call, @NonNull Response<ApiReponseWithNoData> response) {
                Log.d("BorrowHistoryFragment", "Response renew: " + response);

                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    loadBorrowHistory();
                    // Send notification on successful renewal
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
                    if (userLoginInfo != null) {
                        String userId = String.valueOf(userLoginInfo.getUserId());
                        String title = "Gia hạn thành công";
                        String message = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            message = "Bạn đã gia hạn thành công cuốn sách '" + bookTitle + "' đến ngày " + renewalDate.format(DateTimeFormatter.ISO_DATE) + ".";
                        }
                        CustomDialogHelper.showRenewSuccessPopup(getContext(), message);
                        NotificationHelper notificationHelper = new NotificationHelper(getContext());
                        notificationHelper.createNotification(userId, title, message, NotificationType.RENEWAL_SUCCESS.name());
                    }
                } else {
                    String errorMessage = response.body().getMessage();
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                        Log.e("BorrowHistoryFragment", "Renew API failed: " + errorMessage);
                    }
                    CustomDialogHelper.showRenewFailurePopup(getContext(), errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiReponseWithNoData> call, @NonNull Throwable t) {
                CustomDialogHelper.showRenewFailurePopup(getContext(), "Lỗi kết nối khi gia hạn");
                Log.e("BorrowHistoryFragment", "Renew API call failed: " + t.getMessage(), t);
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            rvHistory.setVisibility(View.GONE);
            layoutNoHistory.setVisibility(View.VISIBLE); // Use layoutNoHistory instead of emptyView
        }
    }
}