package com.phamhuong.library.service;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.LoginRequest;
import com.phamhuong.library.model.LoginResponse;
import com.phamhuong.library.model.OtpResetPassRequest;
import com.phamhuong.library.model.RegisterRequest;
import com.phamhuong.library.model.ResetPassRequest;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @GET("books/categories")
    Call<List<Category>> getCategoryAll();
    @GET("books/recent")
    Call<List<Book>> getBookRecent();
    @GET("books/all")
    Call<List<Book>> getAllBooks();
    @GET("books/categories/{genre}")
    Call<List<Book>> getBookByCategory(@Path("genre") String genre);
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @GET("info/{username}")
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
}
