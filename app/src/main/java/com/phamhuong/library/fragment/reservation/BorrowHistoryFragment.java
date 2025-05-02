package com.phamhuong.library.fragment.reservation;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.reservation.BorrowHistoryAdapter;
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
    private RecyclerView recyclerView;
    private BorrowHistoryAdapter adapter;
    private List<BorrowingRecord> histories;
    private ProgressBar progressBar;
    private View emptyView;
    private View layoutNoHistory;
    private ImageView imgNoHistory;
    private TextView tvNoHistory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private APIService apiService;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrow_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        layoutNoHistory = view.findViewById(R.id.layoutNoHistory);
        imgNoHistory = view.findViewById(R.id.imgNoHistory);
        tvNoHistory = view.findViewById(R.id.tvNoHistory);
        // Initialize histories here
        histories = new ArrayList<>();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        notificationHelper = new NotificationHelper(getContext());
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BorrowHistoryAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadBorrowHistory);

        // Load initial data
        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutNoHistory.setVisibility(View.GONE);

        if (getContext() == null) return;
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        int userId = userLoginInfo.getUserId();

        Log.d("BorrowHistoryFragment", "userId: " + userId);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getBorrowingRecordsByUser(userId).enqueue(new Callback<List<BorrowingRecord>>() {
            @Override
            public void onResponse(@NonNull Call<List<BorrowingRecord>> call, @NonNull Response<List<BorrowingRecord>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Log.d("BorrowHistoryFragment", "Response received");

                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowingRecord> records = response.body();
                    Log.d("BorrowHistoryFragment", "Records received: " + records.size());

                    histories.clear();
                    histories.addAll(records);
                    adapter.updateData(records);
                    adapter.notifyDataSetChanged();
                    if (histories.isEmpty()) {
                        layoutNoHistory.setVisibility(View.VISIBLE);
                    } else {
                        layoutNoHistory.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BorrowingRecord>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Log.e("BorrowHistoryFragment", "API call failed: " + t.getMessage(), t);
                recyclerView.setVisibility(View.GONE);
                layoutNoHistory.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.VISIBLE);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.renewBorrowingRecord(recordId, renewalDate).enqueue(new Callback<ApiReponseWithNoData>() {
            @Override
            public void onResponse(@NonNull Call<ApiReponseWithNoData> call, @NonNull Response<ApiReponseWithNoData> response) {
                Log.d("BorrowHistoryFragment", "Response renew: " + response);
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(getContext(), "Gia hạn thành công", Toast.LENGTH_SHORT).show();
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
                progressBar.setVisibility(View.GONE);
                CustomDialogHelper.showRenewFailurePopup(getContext(), "Lỗi kết nối khi gia hạn");
                Log.e("BorrowHistoryFragment", "Renew API call failed: " + t.getMessage(), t);
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            layoutNoHistory.setVisibility(View.VISIBLE); // Use layoutNoHistory instead of emptyView
        }
    }
}