package com.phamhuong.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpRegisterActivity extends AppCompatActivity {

    EditText editTextOtp;
    Button btnVerify;
    APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_otp_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextOtp = findViewById(R.id.editTextOtp);
        btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(view -> {
            VerifyOtp();
        });
    }

    void VerifyOtp(){
        SharedPreferences sharedPreferences = getSharedPreferences("RegisterPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String otp = editTextOtp.getText().toString();
        Map<String,String> requestBody = Map.of("email", email, "otp", otp);

        apiService = RetrofitClient.getRetrofit("").create(APIService.class);
        Call<ApiResponse> call = apiService.activeAccount(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse body = response.body();
                if(body.isStatus()){
                    Toast.makeText(OtpRegisterActivity.this, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(OtpRegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(runnable,1000);
                }else{
                    Toast.makeText(OtpRegisterActivity.this, "Xác nhận thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(OtpRegisterActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
