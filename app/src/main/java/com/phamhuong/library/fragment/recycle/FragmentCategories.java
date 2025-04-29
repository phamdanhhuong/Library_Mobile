package com.phamhuong.library.fragment.recycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.recycle.CategoryAdapter;
import com.phamhuong.library.fragment.book.AllBooksFragment;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategories extends Fragment {
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private CardView btnEbook, btnAudiobook;
    private TextView tvEbook, tvAudiobook;
    private boolean isEbookSelected = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_all_categories, container, false);
        
        initViews(view);
        setupToggleButtons();
        setupRecyclerView();
        loadCategories(true); // Load ebook categories by default
        return view;
    }

    private void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        btnEbook = view.findViewById(R.id.btnEbook);
        btnAudiobook = view.findViewById(R.id.btnAudiobook);
        tvEbook = view.findViewById(R.id.tvEbook);
        tvAudiobook = view.findViewById(R.id.tvAudiobook);
    }

    private void setupToggleButtons() {
        btnEbook.setOnClickListener(v -> {
            if (!isEbookSelected) {
                updateToggleState(true);
                loadCategories(true);
            }
        });

        btnAudiobook.setOnClickListener(v -> {
            if (isEbookSelected) {
                updateToggleState(false);
                loadCategories(false);
            }
        });
    }

    private void updateToggleState(boolean isEbook) {
        isEbookSelected = isEbook;
        
        // Update Ebook button
        tvEbook.setTextColor(isEbook ? 
            Color.WHITE : Color.parseColor("#666666"));
        tvEbook.setBackgroundColor(isEbook ? 
            getResources().getColor(R.color.selectedButton) :
            Color.WHITE);

        // Update Audiobook button
        tvAudiobook.setTextColor(!isEbook ? 
            Color.WHITE : Color.parseColor("#666666"));
        tvAudiobook.setBackgroundColor(!isEbook ? 
            getResources().getColor(R.color.selectedButton) :
            Color.WHITE);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCategories.setLayoutManager(layoutManager);
        
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>(), this::fetchBooksByCategory);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void loadCategories(boolean isEbook) {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call;

        // Nếu isEbook là true, gọi API dành cho Ebook, ngược lại gọi API dành cho Audiobook
        if (isEbook) {
            call = apiService.getEbookCategories(); // API riêng cho Ebook
        } else {
            call = apiService.getAudiobookCategories(); // API riêng cho Audiobook
        }
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter.updateData(response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách thể loại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchBooksByCategory(Category category) {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        // Gửi type tương ứng: "ebook" hoặc "audiobook"
        String type = isEbookSelected ? "ebook" : "audiobook";
        Call<List<Book>> call = apiService.getBooksByCategoryAndType(category.getGenre(), type);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AllBooksFragment fragment = AllBooksFragment.newInstance(category.getGenre(), new ArrayList<>(response.body()));
                    FragmentActivity activity = getActivity();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy sách trong thể loại này", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}