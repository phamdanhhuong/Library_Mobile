package com.phamhuong.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.RegisterRequest;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView txtAlreadyHaveAccount;
    EditText editTextUsername, editTextPassword, editTextFullname, editTextPhonenumber, editTextEmail;
    Button btnSignup;
    APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();


        txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignup.setOnClickListener(view ->{
            Register();
        });
    }

    void init(){
        txtAlreadyHaveAccount = findViewById(R.id.txtAlreadyHaveAccount);
        editTextUsername = findViewById(R.id.etUserName);
        editTextPassword = findViewById(R.id.etPassword);
        editTextFullname = findViewById(R.id.etFullName);
        editTextPhonenumber = findViewById(R.id.etPhone);
        editTextEmail = findViewById(R.id.etEmail);
        btnSignup = findViewById(R.id.btnSignup);
    }

    void Register(){
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String fullName = editTextFullname.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNumberStr = editTextPhonenumber.getText().toString();
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return; // Stop the registration process if any required field is empty
        }
        Long phoneNumber = null;
        if (!phoneNumberStr.isEmpty()) {
            try {
                phoneNumber = Long.parseLong(phoneNumberStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số điện thoại không hợp lệ.", Toast.LENGTH_SHORT).show();
                return; // Stop the registration process if the phone number format is invalid
            }
        } else {
            Log.w("RegisterActivity", "Phone number field is empty.");
        }
        RegisterRequest registerRequest = new RegisterRequest(editTextUsername.getText().toString(), editTextPassword.getText().toString(),
                editTextFullname.getText().toString(), Long.parseLong(editTextPhonenumber.getText().toString()), editTextEmail.getText().toString());
        CallAPI(registerRequest);
    }

    void CallAPI(RegisterRequest registerRequest){

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse body = response.body();
                if(body.isStatus()){
                    saveRegisterInfo(registerRequest.getEmail());
                    SendOtpActiveAccount(registerRequest.getEmail());
                }else{
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void SendOtpActiveAccount(String email){
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Đang gửi OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Map<String,String> requestBody = Map.of("email", email);
        Call<ApiResponse> call = apiService.SendOtpActiveAccount(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                ApiResponse body = response.body();
                if(body.isStatus()){
                    Intent intent = new Intent(RegisterActivity.this, OtpRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Gửi otp thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gửi otp thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveRegisterInfo(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("RegisterPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
