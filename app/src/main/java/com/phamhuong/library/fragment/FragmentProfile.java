package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.BookInfoAdapter;
import com.phamhuong.library.adapter.CategoryAdapter;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.service.APIService;

import java.util.List;

public class FragmentProfile extends Fragment  {
    APIService apiService;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        // Load các Fragment con
        loadFragment(new FragmentUserInfo(), R.id.fragmentUserInfo);
        loadFragment(new FragmentBrownTo(), R.id.fragmentBrownTo);
        loadFragment(new FragmentAnalytics(), R.id.fragmentAnalytics);

        return view;
    }

    private void loadFragment(Fragment fragment, int containerId) {
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        init(view);
//        fetchCategoty();
//        fetchAllBook();
//    }
//
//    public void init(View view){
//        rcCategory = view.findViewById(R.id.rcCategory);
//        rcBook = view.findViewById(R.id.rcBook);
//        txtGenre = view.findViewById(R.id.tvNameCategory);
//
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        token = sharedPreferences.getString("token", "");
//    }
//    private void fetchCategoty(){
//        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
//        Call<List<Category>> call = apiService.getCategoryAll();
//        call.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if(response.isSuccessful()){
//                    ListCategories = response.body();
//                    adapter = new CategoryAdapter(getContext(), ListCategories, FragmentProfile.this);
//                    rcCategory.setAdapter(adapter);
//                    rcCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
//                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    public void fetchBookRecent(){
//        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
//        Call<List<Book>> call = apiService.getBookRecent();
//        call.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if(response.isSuccessful()){
//                    ListBooks = response.body();
//                    bookAdapter = new BookInfoAdapter(getContext(), ListBooks);
//                    rcBook.setAdapter(bookAdapter);
//                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//                    rcBook.setLayoutManager(layoutManager);
//                    rcBook.setHasFixedSize(true);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
//                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void fetchAllBook(){
//        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
//        Call<List<Book>> call = apiService.getAllBooks();
//        call.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if(response.isSuccessful()){
//                    ListBooks = response.body();
//                    bookAdapter = new BookInfoAdapter(getContext(), ListBooks);
//                    rcBook.setAdapter(bookAdapter);
//                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//                    rcBook.setLayoutManager(layoutManager);
//                    rcBook.setHasFixedSize(true);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
//                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void fetchBookByCategory(String genre){
//        apiService = RetrofitClient.getRetrofit(token).create(APIService.class);
//        Call<List<Book>> call = apiService.getBookByCategory(genre);
//        call.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if(response.isSuccessful()){
//                    ListBooks = response.body();
//                    bookAdapter = new BookInfoAdapter(getContext(), ListBooks);
//                    rcBook.setAdapter(bookAdapter);
//                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//                    rcBook.setLayoutManager(layoutManager);
//                    rcBook.setHasFixedSize(true);
//                    txtGenre.setText(genre+"("+ListBooks.size()+")");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Log.d("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
//                Toast.makeText(getContext(), "Lỗi kết nối!" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//
//    @Override
//    public void onCategoryClick(String genre) {
//        fetchBookByCategory(genre);
//    }

}