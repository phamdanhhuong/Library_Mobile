package com.phamhuong.library.fragment.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryBottomSheetFragment extends BottomSheetDialogFragment {
    private FlexboxLayout flexboxCategory;
    private APIService apiService;
    private OnCategorySelectedListener listener;
    private String title;
    private ArrayList<String> categoryNamesList;
    private String selectedCategoryName; // Đổi tên cho rõ ràng

    public interface OnCategorySelectedListener {
        void onCategorySelected(String categoryName);
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_category, container, false);
        flexboxCategory = view.findViewById(R.id.flexboxCategory);
        TextView tvTitle = view.findViewById(R.id.tvTitle);

        if (getArguments() != null) {
            title = getArguments().getString("title");
            categoryNamesList = getArguments().getStringArrayList("category_names");
            selectedCategoryName = getArguments().getString("selected_item");

            // Nhận item đã chọn
            if (title != null) {
                tvTitle.setText(title);
            }
        }

        if (categoryNamesList != null) {
            displayCategoryNames(categoryNamesList);
        } else {
            fetchCategoryNamesFromApi(); // Hoặc fetch toàn bộ Category và lấy tên
        }

        return view;
    }

    private void fetchCategoryNamesFromApi() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = apiService.getCategoryAll();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> names = response.body().stream().map(Category::getGenre).collect(Collectors.toList());
                    displayCategoryNames(names);
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

    private void displayCategoryNames(List<String> categoryNames) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        flexboxCategory.removeAllViews();

        for (String categoryName : categoryNames) {
            TextView categoryView = (TextView) inflater.inflate(R.layout.item_category_chip, flexboxCategory, false);
            categoryView.setText(categoryName);

            // Kiểm tra nếu category này đang được chọn
            if (categoryName != null && categoryName.equals(selectedCategoryName)) {
                categoryView.setBackgroundResource(R.drawable.bg_category_chip_selected); // Sử dụng background màu khác
                categoryView.setTextColor(getResources().getColor(android.R.color.black)); // Sử dụng màu chữ khác
            } else {
                categoryView.setBackgroundResource(R.drawable.filter_button_background); // Background mặc định
                categoryView.setTextColor(getResources().getColor(android.R.color.black)); // Màu chữ mặc định
            }

            categoryView.setOnClickListener(v -> {
                if (listener != null) {
                    selectedCategoryName = categoryName; // Cập nhật category đã chọn
                    listener.onCategorySelected(categoryName);
                    // Cập nhật lại giao diện để hiển thị màu đã chọn
                    displayCategoryNames(categoryNames);
                }
                dismiss();
            });

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            categoryView.setLayoutParams(params);

            flexboxCategory.addView(categoryView);
        }
    }
}