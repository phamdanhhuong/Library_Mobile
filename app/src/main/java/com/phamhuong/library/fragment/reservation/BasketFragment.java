package com.phamhuong.library.fragment.reservation;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.BookBasketAdapter;
import com.phamhuong.library.adapter.recycle.BookHorizontalAdapter;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.ReservationRequest;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;
import com.phamhuong.library.utils.CustomDialogHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketFragment extends Fragment {
    private RecyclerView rvWishList;
    private BookBasketAdapter adapter;
    private List<Book> books;
    private ImageButton btnBack, btnReservation;
    private TextView tvReservationTitle;
    LinearLayout layoutNoWishlist;
    List<Book> selectedBooks;

    public static ReservationDetailFragment newInstance() {
        ReservationDetailFragment fragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        //args

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        initViews(view);
        setupRecyclerView();
        loadWishListBooks();

        return view;
    }

    private void initViews(View view) {
        rvWishList = view.findViewById(R.id.rvReservationBooks);
        btnBack = view.findViewById(R.id.btnBack);
        btnReservation = view.findViewById(R.id.btnReservation);
        tvReservationTitle = view.findViewById(R.id.tvReservationTitle);
        layoutNoWishlist = view.findViewById(R.id.layoutNoWishlist);
        tvReservationTitle.setText("Chi tiết giỏ hàng");

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnReservation.setOnClickListener(v -> {
            if (adapter != null) {
                selectedBooks = adapter.getSelectedBooks();

                if (!selectedBooks.isEmpty()) {
                    // Mở DatePickerDialog nếu có sách được chọn
                    showDatePickerDialog();
                } else {
                    CustomDialogHelper.showNoBookSelectedForReservationPopup(getActivity());
                }
            } else {
                Toast.makeText(getActivity(), "Adapter chưa được khởi tạo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        int userId = userLoginInfo.getUserId();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    // Tạo LocalDateTime từ ngày đã chọn (gán giờ mặc định là 23:59)
                    LocalDateTime selectedDateTime = null;
                    String formattedDate = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        selectedDateTime = LocalDateTime.of(year1, month1 + 1, dayOfMonth, 23, 59);
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        formattedDate = selectedDateTime.format(formatter);
                    }

                    // Lấy danh sách bookId từ selectedBooks
                    List<Integer> bookIds = new ArrayList<>();
                    for (Book book : selectedBooks) {
                        bookIds.add(book.getId()); // đảm bảo Book có getId()
                    }

                    // Tạo đối tượng ReservationRequest
                    ReservationRequest request = new ReservationRequest();
                    request.setUserId(userId);
                    request.setBookIds(bookIds);
                    request.setExpirationDate(formattedDate);

                    // Debug hoặc gọi API
                    Log.d("RESERVATION", "Gửi yêu cầu: " + request.toString());
                    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
                    apiService.reserveBook(request).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if(response.isSuccessful() && response.body() != null){
                                CustomDialogHelper.showReservationSuccessPopup(getActivity());
                                loadWishListBooks();
                            }
                            else {
                                String errorMessage = "Đặt lịch thất bại!";
                                if (response.body() != null && response.body().getMessage() != null) {
                                    errorMessage = response.body().getMessage();
                                }
                                CustomDialogHelper.showReservationFailurePopup(getActivity(), errorMessage);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            CustomDialogHelper.showReservationFailurePopup(getActivity(), "Lỗi kết nối khi đặt lịch");
                        }
                    });

                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();    
    }


    private void setupRecyclerView() {
        books = new ArrayList<>();
        adapter = new BookBasketAdapter(getContext(), books);
        rvWishList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWishList.setAdapter(adapter);
    }

    private void loadWishListBooks() {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        int userId = userLoginInfo.getUserId();

        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getWishListByUserId(userId).enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Response<ApiResponseT<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> bookList = response.body().getData();
                    books.clear();
                    books.addAll(bookList);
                    adapter.notifyDataSetChanged();
                    if (books.isEmpty()) {
                        layoutNoWishlist.setVisibility(View.VISIBLE);
                    } else {
                        layoutNoWishlist.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Throwable t) {
                Log.e("", "Error: " + t.getMessage());
            }
        });
    }
}
