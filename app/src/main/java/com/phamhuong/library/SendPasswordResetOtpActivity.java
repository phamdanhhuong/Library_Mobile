package com.phamhuong.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.utils.CustomDialogHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendPasswordResetOtpActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button btnSendOtp;
    TextView tvBackToLogin;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reset_password_otp);

        init();

        btnSendOtp.setOnClickListener(view -> sendOtp());
        tvBackToLogin.setOnClickListener(view -> navigateToLogin());
    }

    void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
    }

    void sendOtp() {
        ProgressDialog progressDialog = new ProgressDialog(SendPasswordResetOtpActivity.this);
        progressDialog.setMessage("Đang gửi OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        Call<ApiResponseT<String>> call = apiService.sendPasswordResetOtp(requestBody);
        call.enqueue(new Callback<ApiResponseT<String>>() {
            @Override
            public void onResponse(Call<ApiResponseT<String>> call, Response<ApiResponseT<String>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    saveResetInfo(email);
                    CustomDialogHelper.showCustomDialogSuccess(
                            SendPasswordResetOtpActivity.this,
                            "Email đã được gửi!",
                            "Hãy kiểm tra hộp thư của bạn.",
                            (dialog, which) -> {
                            }
                    );
                    navigateToResetPassword();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseT<String>> call, Throwable t) {
                Log.e("SendPasswordResetOtp", "Lỗi gửi OTP: " + t.getMessage());
                Toast.makeText(SendPasswordResetOtpActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void saveResetInfo(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("ResetPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.commit();
    }

    void navigateToResetPassword() {
        Intent intent = new Intent(SendPasswordResetOtpActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }
    void navigateToLogin() {
        Intent intent = new Intent(SendPasswordResetOtpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
