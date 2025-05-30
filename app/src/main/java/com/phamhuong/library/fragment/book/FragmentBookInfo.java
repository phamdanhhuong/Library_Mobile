package com.phamhuong.library.fragment.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.CreateNotificationRequest;
import com.phamhuong.library.model.Notification;
import com.phamhuong.library.model.NotificationType;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.model.VoidResponse;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;
import com.phamhuong.library.utils.CustomDialogHelper;
import com.phamhuong.library.utils.DateFormatter;
import com.phamhuong.library.utils.NotificationHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBookInfo extends Fragment {
    TextView tvBookName, tvBookAuthor, tvBookDescription, tvGenre, tvNumberOfReviews, tvAverageScore;
    ImageView imgBookCover;
    MaterialButton btnAddToWishlist, btnListen;
    Book book;
    TextView tvPublisher, tvPublicationDate, tvQuantity, tvBorrowedCount, tvPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_info, container, false);
        initView(view);
        initData(); // Gọi hàm này để set dữ liệu

        btnAddToWishlist.setOnClickListener(v -> addToWishlist());

        btnListen.setOnClickListener(v -> handleListenButtonClick());
        return view;
    }
    void handleListenButtonClick() {
        if (book != null && book.getAudioUrl() != null && !book.getAudioUrl().isEmpty()) {
            // Chuyển đến trang nghe audiobook (bạn cần implement logic chuyển trang này)
            navigateToAudioBookFragment(book.getId(), book.getTitle(), book.getAuthor(), book.getAudioUrl(), book.getCoverUrl());
        } else {
            // Hiển thị custom dialog thông báo không có bản nghe thử
            CustomDialogHelper.showCustomDialogFail(
                    getActivity(),
                    "Không có bản nghe thử",
                    "Rất tiếc, cuốn sách này hiện chưa có bản nghe thử.",
                    (dialog, which) -> dialog.dismiss()
            );
        }
    }
    private void navigateToAudioBookFragment(int bookId, String bookTitle, String bookAuthor, String audioUrl, String coverUrl) {
        AudioBookFragment fragment = AudioBookFragment.newInstance(bookId, bookTitle, bookAuthor, audioUrl, coverUrl);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
    void addToWishlist() {
        APIService apiServiceWishlist = RetrofitClient.getRetrofit().create(APIService.class);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();

        if (userLoginInfo != null && !userLoginInfo.getUsername().equals("guest")) {
            int userId = dbHelper.getLoginInfoSQLite().getUserId();
            Map<String, Integer> requestBodyWishlist = new HashMap<>();
            requestBodyWishlist.put("user_id", userId);
            requestBodyWishlist.put("book_id", book.getId());

            // Initialize NotificationHelper
            NotificationHelper notificationHelper = new NotificationHelper(getContext());

            apiServiceWishlist.addToWishList(requestBodyWishlist).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body().isStatus()) {
                        CustomDialogHelper.showWishlistSuccessPopup(getActivity());
                        // Gọi hàm tạo thông báo từ NotificationHelper
                        notificationHelper.createNotification(
                                String.valueOf(userId),
                                "Đã thêm vào Yêu thích",
                                "Bạn đã thêm cuốn sách \"" + book.getTitle() + "\" vào danh sách yêu thích.",
                                NotificationType.RECOMMENDATION.name()
                        );

                    } else {
                        CustomDialogHelper.showWishlistFailurePopup(getActivity());
                        Toast.makeText(getContext(), "Thêm vào danh sách yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("", "Error: " + t.getMessage());
                }
            });
        }
        else {
            // Người dùng là khách, hiển thị thông báo
            Toast.makeText(getContext(), "Tính năng này không khả dụng cho khách.", Toast.LENGTH_SHORT).show();
        }
    }
    void initView(View view) {
        tvBookName = view.findViewById(R.id.tvBookName);
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
        tvBookDescription = view.findViewById(R.id.tvDescription);
        imgBookCover = view.findViewById(R.id.bookImage);
        tvGenre = view.findViewById(R.id.tvGenre);
//        tvNumberOfReviews = view.findViewById(R.id.tvNumberOfReviews);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);
        btnAddToWishlist = view.findViewById(R.id.btnAddToWishlist);
        btnListen = view.findViewById(R.id.btnListen);
        tvPublisher = view.findViewById(R.id.tvPublisher);
        tvPublicationDate = view.findViewById(R.id.tvPublicationDate);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvBorrowedCount = view.findViewById(R.id.tvBorrowedCount);
        tvPrice = view.findViewById(R.id.tvPrice);
    }

    void initData() {
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
            tvBookName.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());
            tvBookDescription.setText(book.getSummary());
            tvGenre.setText(book.getGenre());
            //tvNumberOfReviews.setText(book.getNumberOfReviews() + " reviews");
            tvAverageScore.setText(book.getRating() + " ");

            tvPublisher.setText("Nhà xuất bản: " + book.getPublisher());
            tvPublicationDate.setText("Ngày xuất bản: " + DateFormatter.formatDate(book.getPublicationDate()));
            tvQuantity.setText("Còn lại: " + book.getAvailableQuantity() + " / Tổng: " + book.getTotalQuantity());
            tvBorrowedCount.setText("Đã mượn: " + book.getBorrowedCount());

            if (book.getPrice() > 0) {
                tvPrice.setText(String.format("%,d₫", book.getPrice()));
            } else {
                tvPrice.setText("Free");
            }

            String imageUrl = book.getCoverUrl();
            Glide.with(this).load(imageUrl).into(imgBookCover);
        }
    }

    public static FragmentBookInfo newInstance(Book book) {
        FragmentBookInfo fragment = new FragmentBookInfo();
        Bundle args = new Bundle();
        args.putSerializable("book", book);
        fragment.setArguments(args);
        return fragment;
    }
}
