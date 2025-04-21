package com.phamhuong.library.fragment.reservation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationDetailFragment extends Fragment {
    
    private int reservationId;
    private RecyclerView rvReservationBooks;
    private BookHorizontalAdapter adapter;
    private List<Book> books;
    private ImageButton btnBack;
    private TextView tvReservationTitle;
    private TextView tvReservationInfo;
    private Chip chipStatus;

    public static ReservationDetailFragment newInstance(int reservationId) {
        ReservationDetailFragment fragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putInt("reservationId", reservationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservationId = getArguments().getInt("reservationId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_detail, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadReservationBooks();
        
        return view;
    }

    private void initViews(View view) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        rvReservationBooks = view.findViewById(R.id.rvReservationBooks);
        tvReservationInfo = view.findViewById(R.id.tvReservationInfo);
        chipStatus = view.findViewById(R.id.chipStatus);

        toolbar.setTitle("Chi tiết đặt sách #" + reservationId);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        books = new ArrayList<>();
        adapter = new BookHorizontalAdapter(getContext(), books);
        rvReservationBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReservationBooks.setAdapter(adapter);
    }

    private void loadReservationBooks() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Log.d("ReservationDetailFragment", "Reservation ID: " + reservationId);
        apiService.getBooksByReservationId(reservationId).enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Response<ApiResponseT<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> bookList = response.body().getData();
                    books.clear();
                    books.addAll(bookList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Throwable t) {
                Log.e("", "Error: " + t.getMessage());
            }
        });
    }

    private void updateReservationInfo(Reservation reservation) {
        tvReservationInfo.setText(String.format("Đặt ngày: %s\nSố lượng sách: %d",
            formatDate(reservation.getReservationDate().toString()),
            reservation.getBookCount()));

        chipStatus.setText(reservation.getStatus());
        
        int statusColor;
        switch (reservation.getStatus().toLowerCase()) {
            case "pending":
                statusColor = R.color.status_pending;
                break;
            case "confirmed":
                statusColor = R.color.status_confirmed;
                break;
            case "completed":
                statusColor = R.color.status_completed;
                break;
            case "cancelled":
                statusColor = R.color.status_cancelled;
                break;
            default:
                statusColor = R.color.status_pending;
        }

        chipStatus.setChipBackgroundColorResource(statusColor);
        chipStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.onPrimary));
    }
    private String formatDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            Date date = originalFormat.parse(dateString);

            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

}