package com.phamhuong.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.phamhuong.library.model.LoginRequest;
import com.phamhuong.library.model.LoginResponse;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView txtUsername, txtPassword, btnForgotPassword, btnDontHaveAccount;
    Button btnLogin, btnGuest;
    CheckBox cbRememberMe;
    APIService apiService;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        btnForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, SendPasswordResetOtpActivity.class);
            startActivity(intent);
            finish();
        });
        btnDontHaveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        btnGuest = findViewById(R.id.btnGuest); // Initialize btnGuest
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueAsGuest();
            }
        });

    }
    private void init() {
        txtUsername = findViewById(R.id.editTextUsername);
        txtPassword = findViewById(R.id.editTextPassword);
        btnForgotPassword = findViewById(R.id.tvForgotPassword);
        btnDontHaveAccount = findViewById(R.id.tvDontHaveAccount);
        btnLogin = findViewById(R.id.btnLogin);
        cbRememberMe = findViewById(R.id.cbRememberMe);


        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("rememberMe", false);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();

        if (isRemembered && userLoginInfo != null) {
            String savedUsername = userLoginInfo.username;
            String savedPassword = userLoginInfo.password;
            txtUsername.setText(savedUsername);
            txtPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
            Login();
        }
    }

    public void Login(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<LoginResponse> call = apiService.login(new LoginRequest(txtUsername.getText().toString(),txtPassword.getText().toString()));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                LoginResponse body = response.body();
                if (body.getToken() != null) {
                    if (cbRememberMe.isChecked()) {
                        setRememberMe(true);
                    }
                    if (!cbRememberMe.isChecked()) {
                        setRememberMe(false);
                    }
                    saveUserInfo(txtUsername.getText().toString(), txtPassword.getText().toString(), body.getToken());
                    RetrofitClient.currentToken = body.getToken();
                    Log.d("LoginActivity", "Token: " + RetrofitClient.currentToken);
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RetrofitClient.retrofit = null;
    }
    private void continueAsGuest() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isGuest", true);
        editor.apply();

        // Optionally, you can save a special guest user ID or username in DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.clearAllLoginData(); // Clear any previous login info
        dbHelper.saveGuestLoginInfo(); // Implement this method in DatabaseHelper

        Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
        intent.putExtra("isGuest", true); // Indicate guest user
        startActivity(intent);
        finish();
    }
    private void saveUserInfo(String username, String password, String token) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        long rowId = dbHelper.saveLoginInfoSQLite(username, password, token);
    }
    private void setRememberMe(boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", value);
        editor.apply();
    }
}
