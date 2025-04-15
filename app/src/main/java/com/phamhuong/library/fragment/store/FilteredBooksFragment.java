package com.phamhuong.library.fragment.store;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        btnCategory.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), btnCategory);
            popup.getMenu().add("Tất cả thể loại");
            popup.getMenu().add("Văn học");
            popup.getMenu().add("Kinh tế");
            popup.getMenu().add("Tâm lý");
            // Add more categories...
            
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

        // Price Filter
        btnPrice.setOnClickListener(v -> {
            isFreeOnly = !isFreeOnly;
            btnPrice.setSelected(isFreeOnly);
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
}