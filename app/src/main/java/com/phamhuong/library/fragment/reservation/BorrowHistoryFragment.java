package com.phamhuong.library.fragment.reservation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.reservation.BorrowHistoryAdapter;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BorrowHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private BorrowHistoryAdapter adapter;
    private List<BorrowingRecord> histories;
    private ProgressBar progressBar;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private APIService apiService;

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
        emptyView = view.findViewById(R.id.emptyView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // Initialize histories here
        histories = new ArrayList<>();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BorrowHistoryAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadBorrowHistory);

        // Load initial data
        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);

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
                Log.d("BorrowHistoryFragment", "Response received"); // Thêm log này

                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowingRecord> records = response.body();
                    Log.d("BorrowHistoryFragment", "Records received: " + records.toString());

                    if (records != null) {
                        List<BorrowingRecord>borrowingRecords = response.body();
                        histories.clear();
                        histories.addAll(borrowingRecords);
                        adapter.notifyDataSetChanged();
                        adapter.updateData(records);
                        recyclerView.setVisibility(View.VISIBLE); // Đảm bảo dòng này được gọi
                        emptyView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<BorrowingRecord>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Log.e("BorrowHistoryFragment", "API call failed: " + t.getMessage(), t); // In message và stack trace
                showError("Network error");
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            // Show error message (you can implement this according to your app's UI/UX)
            //TextView errorText = emptyView.findViewById(R.id.emptyText);
            //errorText.setText(message);
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}