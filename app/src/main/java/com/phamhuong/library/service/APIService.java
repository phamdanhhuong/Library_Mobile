package com.phamhuong.library.service;

import com.phamhuong.library.model.ApiReponseWithNoData;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.AudioUrlResponse;
import com.phamhuong.library.model.AvailableTotalBooks;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.CreateNotificationRequest;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.LoginRequest;
import com.phamhuong.library.model.LoginResponse;
import com.phamhuong.library.model.Notification;
import com.phamhuong.library.model.PopularGenre;
import com.phamhuong.library.model.RegisterRequest;
import com.phamhuong.library.model.ReservationRequest;
import com.phamhuong.library.model.ResetPassRequest;
import com.phamhuong.library.model.Review;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.ReviewRequest;
import com.phamhuong.library.model.VoidResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("books/categories")
    Call<List<Category>> getCategoryAll();

    @GET("books/recent")
    Call<List<Book>> getBookRecent();

    @GET("books/all")
    Call<List<Book>> getAllBooks();

    @GET("books/categories/{genre}")
    Call<List<Book>> getBookByCategory(@Path("genre") String genre);

    @GET("books/author/{authorName}")
    Call<List<Book>> getBooksByAuthor(@Path("authorName") String authorName);

    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("query") String searchQuery);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("/info/{username}")
    Call<InfoResponse> getInfo(@Path("username") String username);

    @POST("auth/register")
    Call<ApiResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/send-activation-otp")
    Call<ApiResponse> SendOtpActiveAccount(@Body Map<String, String> requestBody);

    @POST("/auth/activate-account")
    Call<ApiResponse> activeAccount(@Body Map<String, String> requestBody);

    @POST("/auth/reset-password")
    Call<ApiResponseT<String>> resetPassword(@Body ResetPassRequest resetPassRequest);

    @POST("/auth/send-password-reset-otp")
    Call<ApiResponseT<String>> sendPasswordResetOtp(@Body Map<String, String> requestBody);

    @POST("reviews/create")
    Call<Void> createReview(@Body ReviewRequest review);

    @GET("reviews/book/{bookId}")
    Call<List<Review>> getReviewsByBook(@Path("bookId") int bookId);

    @GET("reviews/book/{bookId}/average")
    Call<Double> getAverageRating(@Path("bookId") int bookId);

    @GET("reservations/user/{userId}")
    Call<ApiResponseT<List<Reservation>>> getReservationHistoryByUserId(@Path("userId") int userId);

    @GET("reservations/{reservationId}/books")
    Call<ApiResponseT<List<Book>>> getBooksByReservationId(@Path("reservationId") int reservationId);

    @POST("reservations/reserve")
    Call<ApiResponse> reserveBook(@Body ReservationRequest requestBody);

    @GET("borrowings/user/{userId}")
    Call<List<BorrowingRecord>> getBorrowingRecordsByUser(@Path("userId") Integer userId);

    @GET("wishlist/{userId}")
    Call<ApiResponseT<List<Book>>> getWishListByUserId(@Path("userId") int userId);

    @POST("wishlist/add")
    Call<ApiResponse> addToWishList(@Body Map<String, Integer> requestBody);

    @POST("/api/notifications")
    Call<Notification> createNotification(@Body CreateNotificationRequest createNotificationRequest);

    @GET("/api/notifications/user/{userId}")
    Call<List<Notification>> getNotificationsByUser(@Path("userId") Long userId);

    @GET("books/new-in-month")
    Call<List<Book>> getNewBooksInMonth(@Query("year") int year, @Query("month") int month);

    @GET("borrowings/user/{userId}/due-soon")
    Call<List<BorrowingRecord>> getDueSoonBooksByUser(@Query("userId") int userId, @Query("days") int days);

    @POST("borrowings/{recordId}/renew")
    Call<ApiReponseWithNoData> renewBorrowingRecord(@Path("recordId") Integer recordId, @Query("renewalDate") LocalDate renewalDate);

    @GET("/books/audiobooks")
    Call<ApiResponseT<List<Book>>> getAllAudioBooks();

    @GET("/books/audiobooks/categories/{genre}")
    Call<ApiResponse> getAudioBooksByGenre(@Path("genre") String genre);

    @GET("/books/audiobooks/recent")
    Call<ApiResponse> getRecentAudioBooks();

    @GET("/books/{bookId}/audio-url")
    Call<AudioUrlResponse> getAudioUrl(@Path("bookId") int bookId);

    @GET("/books/audiobooks/categories")
    Call<List<Category>> getAudiobookCategories();

    @GET("/books/ebooks/categories")
    Call<List<Category>> getEbookCategories();

    @GET("/books/by-category-and-type")
    Call<List<Book>> getBooksByCategoryAndType(@Query("category") String category, @Query("type") String type);

    @GET("/info/total")
    Call<Integer> getTotalUsers();

    @GET("/books/top-borrowed/monthly")
    Call<ApiResponseT<List<Book>>> getTopBorrowedMonthly(@Query("type") String type);

    @GET("/books/analytics/popular-genres")
    Call<ApiResponseT<List<PopularGenre>>> getPopularGenres();

    @GET("/books/analytics/available-total-books")
    Call<ApiResponseT<AvailableTotalBooks>> getAvailableTotalBooks();

    @GET("/books/new-releases")
    Call<List<Book>> getNewReleases();

    @GET("/books/top-selling")
    Call<List<Book>> getTopSellingBooks();

    @GET("/books/recommended")
    Call<List<Book>> getRecommendedBooks();

    @GET("/books/free")
    Call<List<Book>> getFreeBooks();
    @GET("books/ebooks")
    Call<ApiResponseT<List<Book>>> getAllEBooks();
}