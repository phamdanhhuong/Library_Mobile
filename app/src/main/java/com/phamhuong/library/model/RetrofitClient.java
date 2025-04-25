package com.phamhuong.library.model;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static String currentToken = "" ;
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(currentToken))// Thêm Interceptor vào Retrofit
                    .connectTimeout(20, TimeUnit.SECONDS) // thời gian chờ kết nối
                    .readTimeout(20, TimeUnit.SECONDS)    // thời gian chờ đọc dữ liệu
                    .writeTimeout(20, TimeUnit.SECONDS)   // thời gian chờ ghi dữ liệu (nếu có)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
