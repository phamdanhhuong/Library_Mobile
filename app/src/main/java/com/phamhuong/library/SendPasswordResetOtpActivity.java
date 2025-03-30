package com.phamhuong.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendPasswordResetOtpActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button btnSendOtp;
    APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_reset_password_otp);

        init();

        btnSendOtp.setOnClickListener(view -> sendOtp());
    }

    void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        apiService = RetrofitClient.getRetrofit("").create(APIService.class);
    }

    void sendOtp() {
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        CallAPI(requestBody);
    }

    void CallAPI(Map<String, String> requestBody) {
        Call<ResponseBody> call = apiService.sendPasswordResetOtp(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseText = response.body().string();
                        Log.d("API Response", responseText);

                        if (responseText.equals("Password reset OTP sent to email.")) {
                            saveResetInfo(requestBody.get("email"));
                            navigateToResetPassword();
                        } else {
                            Toast.makeText(SendPasswordResetOtpActivity.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SendPasswordResetOtpActivity.this, "Lỗi xử lý phản hồi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SendPasswordResetOtpActivity.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SendPasswordResetOtpActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void navigateToResetPassword() {
        Intent intent = new Intent(SendPasswordResetOtpActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    void saveResetInfo(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("ResetPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
}
