package com.phamhuong.library.model;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static String currentToken = "" ;
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(currentToken)) // Thêm Interceptor vào Retrofit
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.7:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
