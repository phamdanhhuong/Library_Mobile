package com.phamhuong.library;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.adapter.BookAdapter;
import com.phamhuong.library.adapter.CategoryAdapter;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    APIService apiService;
    List<Category> ListCategories;
    List<Book> ListBooks;
    CategoryAdapter adapter;
    BookAdapter bookAdapter;
    RecyclerView rcCategory;
    RecyclerView rcBook;
    TextView txtGenre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        fetchCategoty();
        fetchBookRecent();
    }

    public void init(){
        rcCategory = findViewById(R.id.rcCategory);
        rcBook = findViewById(R.id.rcBook);
        txtGenre = findViewById(R.id.tvNameCategory);
    }
    private void fetchCategoty(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategoryAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    ListCategories = response.body();
                    adapter = new CategoryAdapter(MainActivity.this, ListCategories);
                    rcCategory.setAdapter(adapter);
                    rcCategory.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void fetchBookRecent(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Book>> call = apiService.getBookRecent();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()){
                    ListBooks = response.body();
                    bookAdapter = new BookAdapter(MainActivity.this, ListBooks);
                    rcBook.setAdapter(bookAdapter);
                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                    rcBook.setLayoutManager(layoutManager);
                    rcBook.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchBookByCategory(String genre){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Book>> call = apiService.getBookByCategory(genre);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()){
                    ListBooks = response.body();
                    bookAdapter = new BookAdapter(MainActivity.this, ListBooks);
                    rcBook.setAdapter(bookAdapter);
                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                    rcBook.setLayoutManager(layoutManager);
                    rcBook.setHasFixedSize(true);
                    txtGenre.setText(genre+"("+ListBooks.size()+")");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}