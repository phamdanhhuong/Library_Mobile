package com.phamhuong.library.fragment.store;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.book.BookRankAdapter;
import com.phamhuong.library.fragment.book.BookFragment;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class FilteredBooksFragment extends Fragment implements CategoryBottomSheetFragment.OnCategorySelectedListener {
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
            showBookTypeBottomSheet();
        });

        // Category Filter
        btnCategory.setOnClickListener(v -> {
            loadCategoriesFromApiForBottomSheet(); // Tải lại categories khi nhấn
        });

        // Price Filter
        btnPrice.setOnClickListener(v -> {
            // Tạm thời vẫn giữ logic cũ cho bộ lọc giá
            isFreeOnly = !isFreeOnly;
            btnPrice.setSelected(isFreeOnly);
            applyFilters();
        });

        // Rating Filter
        btnRating.setOnClickListener(v -> {
            showRatingBottomSheet();
        });
    }

    private void loadCategoriesFromApiForBottomSheet() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategoryAll();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showCategoryBottomSheet(response.body());
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

    private void displayCategoryBottomSheet(List<Category> categories) {
        btnCategory.setOnClickListener(v -> {
            CategoryBottomSheetFragment bottomSheetFragment = new CategoryBottomSheetFragment();
            Bundle bundle = new Bundle();
            ArrayList<String> categoryNames = (ArrayList<String>) categories.stream().map(Category::getGenre).collect(Collectors.toList());
            bundle.putStringArrayList("category_names", categoryNames);
            bundle.putString("title", "Chọn thể loại");
            bottomSheetFragment.setArguments(bundle);
            bottomSheetFragment.setOnCategorySelectedListener(categoryName -> {
                selectedCategory = categoryName;
                btnCategory.setText(selectedCategory);
                btnCategory.setSelected(true);
                // Không cần tìm lại đối tượng Category ở đây nếu bạn chỉ cần tên để lọc
                applyFilters();
            });
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        });
    }
    private void showBookTypeBottomSheet() {
        CategoryBottomSheetFragment bottomSheetFragment = new CategoryBottomSheetFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> bookTypes = new ArrayList<>();
        bookTypes.add("Sách điện tử");
        bookTypes.add("Sách nói");
        bundle.putStringArrayList("category_names", bookTypes);
        bundle.putString("title", "Chọn loại sách");
        bundle.putString("selected_item", isEbook ? "Sách điện tử" : "Sách nói"); // Truyền loại sách đã chọn
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.setOnCategorySelectedListener(categoryName -> {
            isEbook = categoryName.equals("Sách điện tử");
            btnBookType.setText(categoryName);
            btnBookType.setSelected(true);
            applyFilters();
        });
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }

    private void showRatingBottomSheet() {
        CategoryBottomSheetFragment bottomSheetFragment = new CategoryBottomSheetFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> ratings = new ArrayList<>();
        ratings.add("Tất cả");
        ratings.add("4.5★ trở lên");
        ratings.add("4.0★ trở lên");
        bundle.putStringArrayList("category_names", ratings);
        bundle.putString("title", "Chọn xếp hạng");
        String selectedRatingText = "Tất cả";
        if (minRating == 4.5f) selectedRatingText = "4.5★ trở lên";
        else if (minRating == 4.0f) selectedRatingText = "4.0★ trở lên";
        bundle.putString("selected_item", selectedRatingText); // Truyền xếp hạng đã chọn
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.setOnCategorySelectedListener(rating -> {
            if (rating.equals("4.5★ trở lên")) {
                minRating = 4.5f;
            } else if (rating.equals("4.0★ trở lên")) {
                minRating = 4.0f;
            } else {
                minRating = 0f;
            }
            btnRating.setText(rating);
            btnRating.setSelected(minRating > 0);
            applyFilters();
        });
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }

    private void showCategoryBottomSheet(List<Category> categories) {
        CategoryBottomSheetFragment bottomSheetFragment = new CategoryBottomSheetFragment();
        Bundle bundle = new Bundle();
        ArrayList<String> categoryNames = (ArrayList<String>) categories.stream().map(Category::getGenre).collect(Collectors.toList());
        bundle.putStringArrayList("category_names", categoryNames);
        bundle.putString("title", "Chọn thể loại");
        bundle.putString("selected_item", selectedCategory); // Truyền category đã chọn

        Log.d("CategoryBottomSheet", "selectedCategoryName: " + selectedCategory);
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.setOnCategorySelectedListener(categoryName -> {
            selectedCategory = categoryName;
            btnCategory.setText(selectedCategory);
            btnCategory.setSelected(true);
            applyFilters();
        });
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }
    private int getSelectedCategoryId(String selectedCategoryName, List<Category> categories) {
        if (selectedCategoryName != null) {
            for (Category category : categories) {
                if (category.getGenre().equals(selectedCategoryName)) {
                    return category.getId();
                }
            }
        }
        return -1; // Trả về -1 nếu không có category nào được chọn
    }

    @Override
    public void onCategorySelected(String categoryName) {
        selectedCategory = categoryName;
        btnCategory.setText(selectedCategory);
        btnCategory.setSelected(true);
        applyFilters();
    }
}