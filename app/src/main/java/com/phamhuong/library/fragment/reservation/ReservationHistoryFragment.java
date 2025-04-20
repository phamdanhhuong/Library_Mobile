package com.phamhuong.library.fragment.reservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;;
import com.phamhuong.library.adapter.reservation.ReservationHistoryAdapter;
import com.phamhuong.library.model.ApiResponse;
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

public class ReservationHistoryFragment extends Fragment {
    private RecyclerView rvBorrowHistory;
    private ReservationHistoryAdapter adapter;
    private List<Reservation> histories;
    private ImageButton btnBack;
    APIService apiService;

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

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
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
                .replace(R.id.content_frame, detailFragment)
                .addToBackStack(null)
                .commit();

    }

    private void loadBorrowHistory() {
        if (getContext() == null) return;
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
//        int userId = sharedPreferences.getInt("userId", -1);
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
}