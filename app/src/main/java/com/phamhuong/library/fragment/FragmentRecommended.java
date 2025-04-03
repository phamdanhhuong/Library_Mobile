package com.phamhuong.library.fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.BookInfoAdapter;
import com.phamhuong.library.adapter.BookAdapterRecommended;
import com.phamhuong.library.adapter.BookAdapterRecommended.OnBookClickListener;


import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
public class FragmentRecommended extends Fragment {
    APIService apiService;
    List<Book> ListBooks;
    private ViewPager2 viewPager;
    private BookAdapterRecommended bookAdapter;
    private List<Book> bookList;
    private Handler handler = new Handler();
    private Runnable autoScrollRunnable;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        viewPager = view.findViewById(R.id.viewPagerRecommended);

        fetchAllBook();


        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % bookList.size();
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000); // Chuyển sau mỗi 3 giây
            }
        };
        handler.postDelayed(autoScrollRunnable, 3000);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(autoScrollRunnable);
    }

    public void fetchAllBook(){
        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
        Call<List<Book>> call = apiService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful()){
                    bookList = response.body();
                    bookAdapter = new BookAdapterRecommended(getContext(), bookList, new OnBookClickListener() {
                        @Override
                        public void onBookClick(Book book) {
                            Intent intent = new Intent(getContext(), BookInfoAdapter.class);
                            intent.putExtra("bookId", book.getId());
                            startActivity(intent);
                        }
                    } );

                    viewPager.setAdapter(bookAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
