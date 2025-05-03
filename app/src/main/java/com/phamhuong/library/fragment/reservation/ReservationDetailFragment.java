package com.phamhuong.library.fragment.reservation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.phamhuong.library.R;
import com.phamhuong.library.SendPasswordResetOtpActivity;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.utils.CustomDialogHelper;

import java.io.Serializable;
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
    private Reservation reservation;
    private Button btnCancelReservation;

    public static ReservationDetailFragment newInstance(int reservationId, Reservation reservation) {
        ReservationDetailFragment fragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putInt("reservationId", reservationId);
        args.putSerializable("reservation", reservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservationId = getArguments().getInt("reservationId");
            reservation = (Reservation) getArguments().getSerializable("reservation");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_detail, container, false);
        
        initViews(view);
        setupRecyclerView();
        btnCancelReservation = view.findViewById(R.id.btnCancelReservation); // Ánh xạ nút hủy
        if (reservation != null && !reservation.getStatus().equalsIgnoreCase("cancelled") && !reservation.getStatus().equalsIgnoreCase("completed")) {
            btnCancelReservation.setOnClickListener(v -> cancelReservation());
        } else {
            btnCancelReservation.setVisibility(View.GONE); // Ẩn nút nếu đã hủy hoặc hoàn thành
        }
        if (reservation != null) {
            updateReservationInfo(reservation);
        }
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
    private void cancelReservation() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponse> call = apiService.cancelReservation(reservationId);
        Log.d("ReservationDetailFragment", "Reservation ID: " + reservationId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    CustomDialogHelper.showCustomDialogSuccess(
                            getContext(),
                            "Đã hủy đặt lịch thành công!",
                            "Hãy kiểm tra lại lịch sử đặt lịch.",
                            (dialog, which) -> {
                            }
                    );
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    CustomDialogHelper.showCustomDialogFail(
                        getContext(),
                        "Không thể hủy đặt lịch!",
                        "Hãy kiểm tra lại lịch sử đặt lịch.",
                        (dialog, which) -> {
                        }
                );
                    Log.e("ReservationDetail", "Hủy đặt lịch thất bại: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi hủy đặt lịch", Toast.LENGTH_SHORT).show();
                Log.e("ReservationDetail", "Lỗi kết nối hủy đặt lịch: " + t.getMessage());
            }
        });
    }
    private void loadReservationBooks() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Log.d("ReservationDetailFragment", "Reservation ID: " + reservationId);
        apiService.getBooksByReservationId(reservationId).enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("DefaultLocale")
    private void updateReservationInfo(Reservation reservation) {
        tvReservationInfo.setText(String.format("Đặt ngày: %s\nSố lượng sách: %d",
            formatDate(reservation.getReservationDate()),
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
        loadReservationBooks();
    }
    public String formatDate(String dateString) {
        try {Log.d("ReservationAdapter", "Chuỗi ngày nhận được: " + dateString);
            if (dateString != null) {
                // Định dạng của chuỗi ngày thực tế
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date date = originalFormat.parse(dateString);
                // Định dạng ngày tháng mà bạn muốn hiển thị
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return targetFormat.format(date);
            } else {
                return "Không rõ ngày";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi định dạng"; // Hoặc trả về dateString gốc nếu bạn muốn
        }
    }
}