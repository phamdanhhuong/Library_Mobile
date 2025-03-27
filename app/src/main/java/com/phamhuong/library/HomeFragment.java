package com.phamhuong.library;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.adapter.BookAdapter;
import com.phamhuong.library.adapter.CategoryAdapter;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.OnCategoryClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCategoryClickListener {
    APIService apiService;
    List<Category> ListCategories;
    List<Book> ListBooks;
    CategoryAdapter adapter;
    BookAdapter bookAdapter;
    RecyclerView rcCategory;
    RecyclerView rcBook;
    TextView txtGenre;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        init();
//        fetchCategoty();
//        fetchBookRecent();
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        fetchCategoty();
        fetchBookRecent();
    }

    public void init(View view){
        rcCategory = view.findViewById(R.id.rcCategory);
        rcBook = view.findViewById(R.id.rcBook);
        txtGenre = view.findViewById(R.id.tvNameCategory);
    }
    private void fetchCategoty(){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategoryAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    ListCategories = response.body();
                    adapter = new CategoryAdapter(getContext(), ListCategories, HomeFragment.this);
                    rcCategory.setAdapter(adapter);
                    rcCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    bookAdapter = new BookAdapter(getContext(), ListBooks);
                    rcBook.setAdapter(bookAdapter);
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                    rcBook.setLayoutManager(layoutManager);
                    rcBook.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    bookAdapter = new BookAdapter(getContext(), ListBooks);
                    rcBook.setAdapter(bookAdapter);
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                    rcBook.setLayoutManager(layoutManager);
                    rcBook.setHasFixedSize(true);
                    txtGenre.setText(genre+"("+ListBooks.size()+")");
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCategoryClick(String genre) {
        fetchBookByCategory(genre);
    }

}