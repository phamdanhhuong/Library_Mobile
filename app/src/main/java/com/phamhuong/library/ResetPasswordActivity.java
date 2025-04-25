package com.phamhuong.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.ResetPassRequest;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText editTextOtp, editTextNewPassword;
    Button btnResetPassword;
    APIService apiService;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();

        btnResetPassword.setOnClickListener(view -> resetPassword());
    }

    void init() {
        editTextOtp = findViewById(R.id.editTextOtp);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

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

        ResetPassRequest requestBody = new ResetPassRequest(email, otp, newPassword);

        CallAPI(requestBody);
    }

    void CallAPI(ResetPassRequest requestBody) {
        Call<ApiResponseT<String>> call = apiService.resetPassword(requestBody);
        call.enqueue(new Callback<ApiResponseT<String>>() {
            @Override
            public void onResponse(Call<ApiResponseT<String>> call, Response<ApiResponseT<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseT<String>> call, Throwable t) {
                Log.e("CallAPI", "Lỗi kết nối: " + t.getMessage());
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
