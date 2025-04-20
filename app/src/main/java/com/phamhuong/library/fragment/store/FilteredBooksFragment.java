package com.phamhuong.library.fragment.store;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.phamhuong.library.R;
import com.phamhuong.library.adapter.book.BookRankAdapter;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class FilteredBooksFragment extends Fragment {
    protected RecyclerView rvBooks;
    protected BookRankAdapter adapter;
    protected List<Book> allBooks = new ArrayList<>();
    protected List<Book> filteredBooks = new ArrayList<>();
    
    // Filter views
    protected TextView btnBookType;
    protected TextView btnCategory;
    protected TextView btnPrice;
    protected TextView btnRating;
    
    // Filter states
    protected boolean isEbook = true;
    protected String selectedCategory = null;
    protected boolean isFreeOnly = false;
    protected float minRating = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtered_books, container, false);
        
        initViews(view);
        setupFilters();
        loadCategoriesFromApi();
        setupRecyclerView();
        loadBooks();

        return view;
    }

    private void initViews(View view) {
        rvBooks = view.findViewById(R.id.rvBooks);
        btnBookType = view.findViewById(R.id.btnBookType);
        btnCategory = view.findViewById(R.id.btnCategory);
        btnPrice = view.findViewById(R.id.btnPrice);
        btnRating = view.findViewById(R.id.btnRating);

        setupFilterClickListeners();
    }

    private void setupFilterClickListeners() {
        // Book Type Filter
        btnBookType.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), btnBookType);
            popup.getMenu().add("Sách điện tử");
            popup.getMenu().add("Sách nói");
            
            popup.setOnMenuItemClickListener(item -> {
                isEbook = item.getTitle().toString().equals("Sách điện tử");
                btnBookType.setText(item.getTitle());
                btnBookType.setSelected(true);
            applyFilters();
                return true;
            });
            popup.show();
        });

        // Category Filter
//        btnCategory.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(requireContext(), btnCategory);
//            popup.getMenu().add("Tất cả thể loại");
//
//            for (String category : categories) {
//                popup.getMenu().add(category);
//            }
//
//            popup.setOnMenuItemClickListener(item -> {
//                String category = item.getTitle().toString();
//                selectedCategory = category.equals("Tất cả thể loại") ? null : category;
//                btnCategory.setText(category);
//                btnCategory.setSelected(selectedCategory != null);
//                applyFilters();
//                return true;
//            });
//
//            popup.show();
//        });

        // Price Filter
        btnPrice.setOnClickListener(v -> {
            isFreeOnly = !isFreeOnly;
            btnPrice.setSelected(isFreeOnly);
//            if (isFreeOnly) {
//                animateButtonWidth(btnPrice, btnPrice.getWidth(), 50);
//            }
//            else {
//                animateButtonWidth(btnPrice, btnPrice.getWidth(),  50);
//            }
            applyFilters();
        });

        // Rating Filter
        btnRating.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), btnRating);
            popup.getMenu().add("Tất cả");
            popup.getMenu().add("4.5★ trở lên");
            popup.getMenu().add("4.0★ trở lên");

            popup.setOnMenuItemClickListener(item -> {
                String rating = item.getTitle().toString();
                switch (rating) {
                    case "4.5★ trở lên":
                        minRating = 4.5f;
                        break;
                    case "4.0★ trở lên":
                        minRating = 4.0f;
                        break;
                    default:
                        minRating = 0f;
                        break;
                }
                btnRating.setText(rating);
                btnRating.setSelected(minRating > 0);
                applyFilters();
                return true;
            });

            popup.show();
        });
    }

    private void setupRecyclerView() {
        adapter = new BookRankAdapter(getContext(), filteredBooks, isEbook, 
            book -> {
                // Handle book click
                BookFragment fragment = new BookFragment();
                Bundle args = new Bundle();
                args.putSerializable("book", book);
                fragment.setArguments(args);

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            });

        rvBooks.setAdapter(adapter);
        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void applyFilters() {
        filteredBooks = allBooks.stream()
            .filter(book -> {
                // Book type filter
                if (isEbook != book.isEbook()) return false;

                // Category filter
                if (selectedCategory != null && !book.getGenre().equals(selectedCategory)) return false;

                // Price filter
                if (isFreeOnly && book.getPrice() > 0) return false;

                // Rating filter
                if (book.getRating() < minRating) return false;

                return true;
            })
            .collect(Collectors.toList());

        adapter.updateData(filteredBooks);
    }

    protected abstract void loadBooks();
    protected abstract void setupFilters();
    protected abstract void setupView();
    private void animateButtonWidth(View view, int fromDp, int toDp) {
        int fromPx = (int) (fromDp * getResources().getDisplayMetrics().density);
        int toPx = (int) (toDp * getResources().getDisplayMetrics().density);

        ValueAnimator animator = ValueAnimator.ofInt(fromPx, toPx);
        animator.setDuration(300); // thời gian hiệu ứng (ms)
        animator.addUpdateListener(animation -> {
            int val = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = val;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }
    private void updateCategoryMenu(List<String> categories) {
        btnCategory.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), btnCategory);
            popup.getMenu().add("Tất cả thể loại");
            for (String category : categories) {
                popup.getMenu().add(category);
            }

            popup.setOnMenuItemClickListener(item -> {
                String category = item.getTitle().toString();
                selectedCategory = category.equals("Tất cả thể loại") ? null : category;
                btnCategory.setText(category);
                btnCategory.setSelected(selectedCategory != null);
                applyFilters();
                return true;
            });

            popup.show();
        });
    }
    private void loadCategoriesFromApi() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategoryAll(); // Có thể sửa thành API riêng cho Ebook/Audiobook nếu cần

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> categories = response.body().stream()
                            .map(Category::getGenre)
                            .collect(Collectors.toList());
                    updateCategoryMenu(categories);
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
}