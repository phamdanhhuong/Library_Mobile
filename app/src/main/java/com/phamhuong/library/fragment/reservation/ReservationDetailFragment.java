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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

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
        rvReservationBooks = view.findViewById(R.id.rvReservationBooks);
        btnBack = view.findViewById(R.id.btnBack);
        tvReservationTitle = view.findViewById(R.id.tvReservationTitle);

        tvReservationTitle.setText("Chi tiết đặt sách #" + reservationId);

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
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
}
