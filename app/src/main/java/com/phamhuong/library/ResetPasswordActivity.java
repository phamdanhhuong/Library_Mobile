package com.phamhuong.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText editTextOtp, editTextNewPassword;
    Button btnResetPassword;
    APIService apiService;
    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        init();

        btnResetPassword.setOnClickListener(view -> resetPassword());
    }

    void init() {
        editTextOtp = findViewById(R.id.editTextOtp);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        apiService = RetrofitClient.getRetrofit("").create(APIService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("ResetPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
    }

    void resetPassword() {
        String otp = editTextOtp.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();

        if (otp.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("otp", otp);
        requestBody.put("newPassword", newPassword);

        CallAPI(requestBody);
    }

    void CallAPI(Map<String, String> requestBody) {
        Call<String> call = apiService.resetPassword(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void navigateToLogin() {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
