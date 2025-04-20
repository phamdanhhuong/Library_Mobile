package com.phamhuong.library.model;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static String currentToken = "" ;
    public static Retrofit getRetrofit(String token){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token)) // Thêm Interceptor vào Retrofit
                .build();
        if(retrofit == null || currentToken != token){
            currentToken = token;
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.3:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
