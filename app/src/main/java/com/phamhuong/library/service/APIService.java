package com.phamhuong.library.service;

import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.InfoResponse;
import com.phamhuong.library.model.LoginRequest;
import com.phamhuong.library.model.LoginResponse;

import java.util.List;

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
    @GET("books/categories/{genre}")
    Call<List<Book>> getBookByCategory(@Path("genre") String genre);
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("info/{username}")
    Call<InfoResponse> getInfo(@Path("username") String username);
}
