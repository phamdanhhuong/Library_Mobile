package com.phamhuong.library.fragment.reservation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.reservation.ReservationHistoryAdapter;
import com.phamhuong.library.fragment.book.AllBooksFragment;
import com.phamhuong.library.model.ApiResponseT;
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
    private RecyclerView rvBorrowHistory;
    private ReservationHistoryAdapter adapter;
    private List<Reservation> histories;
    private ImageButton btnBack;
    private View loadingView;
    private View errorView;
    private ViewGroup contentView;
    private View emptyView;
    private ShimmerFrameLayout shimmerLayout;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_history, container, false);

        initViews(view);
        setupRecyclerView();
        loadBorrowHistory();

        return view;
    }

    private void initViews(View view) {
        rvBorrowHistory = view.findViewById(R.id.rvReservations);
        btnBack = view.findViewById(R.id.btnBack);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        loadingView = view.findViewById(R.id.loadingState);
        errorView = view.findViewById(R.id.errorState);
        contentView = view.findViewById(R.id.contentLayout);

        Button btnRetry = errorView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> loadReservations());
        emptyView = view.findViewById(R.id.emptyState);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        view.findViewById(R.id.btnBrowseBooks).setOnClickListener(v -> {
            // Navigate to book browsing screen
            navigateToBookBrowsing();
        });
    }

    private void setupRecyclerView() {
        histories = new ArrayList<>();
        adapter = new ReservationHistoryAdapter(getContext(), histories, this::onReservationClick);
        rvBorrowHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBorrowHistory.setAdapter(adapter);
    }

    private void onReservationClick(Reservation reservation) {
        Integer reservationId = reservation.getReservationId();
        if (reservationId == null) {
            Log.e("ReservationHistory", "reservationId is null!");
            return;
        }

        ReservationDetailFragment detailFragment = ReservationDetailFragment.newInstance(reservationId);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                )
                .replace(R.id.content_frame, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadBorrowHistory() {
        if (getContext() == null) return;
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        int userId = userLoginInfo.getUserId();

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getReservationHistoryByUserId(userId).enqueue(new Callback<ApiResponseT<List<Reservation>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Reservation>>> call, @NonNull Response<ApiResponseT<List<Reservation>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Reservation> reservationList = response.body().getData();
                    histories.clear();
                    histories.addAll(reservationList);
                    Log.d("ReservationHistoryFragment", "Histories size: " + histories.size());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Reservation>>> call, @NonNull Throwable t) {
                Log.e("ReservationHistoryFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void showLoading() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }

    private void showEmpty() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void showContent() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }


    private void loadReservations() {
        showLoading();

        int userId = getUserId();
        if (userId == -1) {
            showError("Vui lòng đăng nhập để xem lịch sử đặt sách");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getReservationHistoryByUserId(userId).enqueue(new Callback<ApiResponseT<List<Reservation>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Reservation>>> call, @NonNull Response<ApiResponseT<List<Reservation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reservation> reservationsList = response.body().getData();
                    histories.clear();
                    histories.addAll(reservationsList);
                    adapter.notifyDataSetChanged();

                    if (histories.isEmpty()) {
                        showError("Bạn chưa có đơn đặt sách nào");
                    } else {
                        showContent();
                    }
                } else {
                    showError("Không thể tải lịch sử đặt sách");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Reservation>>> call, @NonNull Throwable t) {
                showError("Đã có lỗi xảy ra. Vui lòng thử lại");
            }
        });
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }
    private void navigateToBookBrowsing() {
        // Implement navigation to book browsing screen
        // Example:
        Fragment browseFragment = new AllBooksFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, browseFragment)
                .addToBackStack(null)
                .commit();
    }
    private void showError(String message) {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);

        TextView tvErrorMessage = errorView.findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setText(message);
    }

}
