package com.phamhuong.library.service;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.LoginRequest;
import com.phamhuong.library.model.LoginResponse;
import com.phamhuong.library.model.RegisterRequest;
import com.phamhuong.library.model.ReservationRequest;
import com.phamhuong.library.model.ResetPassRequest;
import com.phamhuong.library.model.Review;
import com.phamhuong.library.model.Reservation;
import com.phamhuong.library.model.ReviewRequest;

import java.util.List;
import java.util.Map;

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
    Call<ResponseBody> resetPassword(@Body ResetPassRequest resetPassRequest);
    @POST("/auth/send-password-reset-otp")
    Call<ResponseBody> sendPasswordResetOtp(@Body Map<String, String> requestBody);
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
    @GET("wishlist/{userId}")
    Call<ApiResponseT<List<Book>>> getWishListByUserId(@Path("userId") int userId);
    @POST("wishlist/add")
    Call<ApiResponse> addToWishList(@Body Map<String, Integer> requestBody);
}
