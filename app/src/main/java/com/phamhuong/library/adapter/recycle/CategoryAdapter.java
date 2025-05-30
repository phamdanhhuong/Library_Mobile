package com.phamhuong.library.adapter.recycle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Category;
import com.phamhuong.library.service.OnCategoryClickListener;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private boolean isEbookSelected;

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener, boolean isEbookSelected) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
        this.isEbookSelected = isEbookSelected;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    public List<Category> getCategories() {
        return categories;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_grid, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, isEbookSelected);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryName;
        TextView tvBookCount;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
        }

        @SuppressLint("SetTextI18n")
        void bind(Category category, boolean isEbookSelected) {
            tvCategoryName.setText(category.getGenre());
            if (isEbookSelected) {
                tvBookCount.setText(category.getEBookCount() + " ebooks");
            } else {
                tvBookCount.setText(category.getAudioBookCount() + " audiobooks");
            }
            
            // Load category image using Glide
            Glide.with(context)
                .load(category.getImages())
                .placeholder(R.drawable.book_placeholder)
                .into(imgCategory);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
    public void updateData(List<Category> newCategories, boolean isEbookSelected) {
        categories.clear();
        categories.addAll(newCategories);
        this.isEbookSelected = isEbookSelected;
        notifyDataSetChanged();
    }
}
