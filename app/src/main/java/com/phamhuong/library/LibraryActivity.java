package com.phamhuong.library;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        TextView textView = findViewById(R.id.tvCategory);
        String category = getIntent().getStringExtra("CATEGORY");
        textView.setText("Thư viện: " + (category != null ? category : "Unknown"));
    }
}