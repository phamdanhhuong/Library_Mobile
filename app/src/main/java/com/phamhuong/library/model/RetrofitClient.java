package com.phamhuong.library.model;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static Retrofit getRetrofit(String token){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token)) // Thêm Interceptor vào Retrofit
                .build();
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
