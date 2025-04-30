package com.phamhuong.library.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.AvailableTotalBooks;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.model.PopularGenre;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnalytics extends Fragment {

    private TextView totalBooksTextView;
    private TextView totalUsersTextView;
    private TextView totalGenresTextView;
    private ImageView imgTop1, imgTop2, imgTop3;
    private Button btnEbook, btnAudiobook;
    private BarChart popularGenresBarChart;
    PieChart popularGenresPieChart,availableTotalPieChart;
    private List<Book> topBorrowedBooks;
    private String currentBookType = "ebook";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get references to TextViews
        totalBooksTextView = view.findViewById(R.id.analyticsTotalBooksTextView);
        totalUsersTextView = view.findViewById(R.id.analyticsTotalUsersTextView);
        totalGenresTextView = view.findViewById(R.id.analyticsTotalGenresTextView);
        imgTop1 = view.findViewById(R.id.imgTop1);
        imgTop2 = view.findViewById(R.id.imgTop2);
        imgTop3 = view.findViewById(R.id.imgTop3);
        popularGenresPieChart = view.findViewById(R.id.analyticsPopularGenresPieChart);
        availableTotalPieChart = view.findViewById(R.id.analyticsAvailableTotalPieChart);
        btnEbook = view.findViewById(R.id.btnEbook);
        btnAudiobook = view.findViewById(R.id.btnAudiobook);

        loadTopBorrowedMonthly(currentBookType); // Load top ebooks initially

        // Set listeners for the buttons
        btnEbook.setOnClickListener(v -> {
            currentBookType = "ebook";
            loadTopBorrowedMonthly(currentBookType);
        });

        btnAudiobook.setOnClickListener(v -> {
            currentBookType = "audiobook";
            loadTopBorrowedMonthly(currentBookType);
        });
        // Load data for each statistic
        loadTotalBooks();
        loadTotalUsers(); // Implement this method
        loadTotalGenres(); // Implement this method
        loadPopularGenres();
        //loadAvailableTotal();
        // ... Load data for other statistics (Top Borrowed, Recent Books, etc.)
    }

    private void loadTotalBooks() {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Book>> call = service.getAllBooks(); // Adjust API endpoint if needed

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> allBooks = response.body();
                    totalBooksTextView.setText(String.valueOf(allBooks.size()));
                } else {
                    Log.e("FragmentAnalytics", "Failed to load total books: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading total books: " + t.getMessage());
            }
        });
    }

    private void loadTotalUsers() {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<Integer> call = service.getTotalUsers();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int totalUsers = response.body();
                    totalUsersTextView.setText(String.valueOf(totalUsers));
                } else {
                    Log.e("FragmentAnalytics", "Failed to load total users: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading total users: " + t.getMessage());
            }
        });
    }

    private void loadTotalGenres() {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<List<Category>> call = service.getCategoryAll(); // Adjust API endpoint if needed

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> allGenres = response.body();
                    totalGenresTextView.setText(String.valueOf(allGenres.size()));
                } else {
                    Log.e("FragmentAnalytics", "Failed to load total genres: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading total genres: " + t.getMessage());
            }
        });
    }
    private void loadTopBorrowedMonthly(String bookType) {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponseT<List<Book>>> call = service.getTopBorrowedMonthly(bookType);

        call.enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Response<ApiResponseT<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    topBorrowedBooks = response.body().getData();
                    displayTopBorrowedBooks();
                } else {
                    Log.e("FragmentAnalytics", "Failed to load top borrowed books for " + bookType + ": " + response.message());
                    Toast.makeText(getContext(), "Không thể tải top sách mượn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading top borrowed books for " + bookType + ": " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi mạng khi tải top sách mượn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTopBorrowedBooks() {
        if (topBorrowedBooks != null && isAdded()) { // Check if the Fragment is attached
            ImageView[] topImageViews = {imgTop1, imgTop2, imgTop3};
            for (int i = 0; i < Math.min(topBorrowedBooks.size(), topImageViews.length); i++) {
                Book book = topBorrowedBooks.get(i);
                if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                    Glide.with(requireContext()) // Now it's safe to call requireContext()
                            .load(book.getCoverUrl())
                            .placeholder(R.drawable.book_placeholder)
                            .error(R.drawable.book_placeholder)
                            .into(topImageViews[i]);
                } else {
                    topImageViews[i].setImageResource(R.drawable.book_placeholder);
                }
            }
            // Clear remaining image views if fewer than 3 books
            for (int i = topBorrowedBooks.size(); i < topImageViews.length; i++) {
                topImageViews[i].setImageResource(R.drawable.book_placeholder); // Or set visibility to GONE
            }
        }
    }
    private void loadPopularGenres() {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponseT<List<PopularGenre>>> call = service.getPopularGenres();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<PopularGenre>>> call, @NonNull Response<ApiResponseT<List<PopularGenre>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<PopularGenre> popularGenresList = response.body().getData();
                    if (popularGenresList != null && !popularGenresList.isEmpty()) {
                        displayPopularGenresChart(popularGenresList);
                    } else {
                        Log.w("FragmentAnalytics", "No popular genres data received.");
                    }
                } else {
                    Log.e("FragmentAnalytics", "Failed to load popular genres: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<PopularGenre>>> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading popular genres: " + t.getMessage());
            }
        });
    }

    private void displayPopularGenresChart(List<PopularGenre> popularGenresList) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (PopularGenre popularGenre : popularGenresList) {
            entries.add(new PieEntry(popularGenre.getCount(), popularGenre.getGenre()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Use predefined color template
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(popularGenresPieChart)); // Use the BarChart for formatting as it's available

        popularGenresPieChart.setData(pieData);

        // Customize the chart's appearance (optional)
        popularGenresPieChart.getDescription().setEnabled(false);
        popularGenresPieChart.setDrawHoleEnabled(true);
        popularGenresPieChart.setHoleColor(Color.WHITE);
        popularGenresPieChart.setHoleRadius(58f);
        popularGenresPieChart.setTransparentCircleRadius(61f);

        popularGenresPieChart.setDrawEntryLabels(true);
        popularGenresPieChart.setEntryLabelColor(Color.BLACK);
        popularGenresPieChart.setEntryLabelTextSize(12f);

        popularGenresPieChart.animateY(1000, Easing.EaseInOutQuad);

        popularGenresPieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        popularGenresPieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        popularGenresPieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        popularGenresPieChart.getLegend().setDrawInside(false);

        popularGenresPieChart.invalidate(); // Refresh the chart
    }
    private void loadAvailableTotal() {
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponseT<AvailableTotalBooks>> call = service.getAvailableTotalBooks();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<AvailableTotalBooks>> call, @NonNull Response<ApiResponseT<AvailableTotalBooks>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    AvailableTotalBooks availableTotal = response.body().getData();
                    if (availableTotal != null) {
                        displayAvailableTotalChart(availableTotal.getTotalBooks(), availableTotal.getAvailableBooks());
                    } else {
                        Log.w("FragmentAnalytics", "No available/total books data received.");
                    }
                } else {
                    Log.e("FragmentAnalytics", "Failed to load available/total books: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải trạng thái sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<AvailableTotalBooks>> call, @NonNull Throwable t) {
                Log.e("FragmentAnalytics", "Error loading available/total books: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi mạng khi tải trạng thái sách", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayAvailableTotalChart(int totalBooks, int availableBooks) {
        if (isAdded()) { // Check if the Fragment is attached
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(availableBooks, "Hiện có")); // Label for available books
            entries.add(new PieEntry(totalBooks - availableBooks, "Đã mượn")); // Label for borrowed books

            PieDataSet dataSet = new PieDataSet(entries, "Trạng thái sách");
            dataSet.setColors(new int[]{ContextCompat.getColor(requireContext(), R.color.available_color),
                    ContextCompat.getColor(requireContext(), R.color.borrowed_color)}); // Define these colors in colors.xml
            dataSet.setValueTextSize(12f);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setValueFormatter(new PercentFormatter(availableTotalPieChart));

            PieData pieData = new PieData(dataSet);
            availableTotalPieChart.setData(pieData);

            // Customize the chart's appearance (optional)
            availableTotalPieChart.getDescription().setEnabled(false);
            availableTotalPieChart.setDrawEntryLabels(true); // Display labels on the slices
            availableTotalPieChart.setUsePercentValues(true); // Display percentage values
            availableTotalPieChart.animateY(1000, Easing.EaseInOutQuad);
            availableTotalPieChart.setDrawHoleEnabled(true);
            availableTotalPieChart.setHoleColor(Color.WHITE);
            availableTotalPieChart.setHoleRadius(58f);
            availableTotalPieChart.setTransparentCircleRadius(61f);
            availableTotalPieChart.getLegend().setEnabled(false); // Hide legend for simplicity

            availableTotalPieChart.invalidate(); // Refresh the chart
        }
    }
}