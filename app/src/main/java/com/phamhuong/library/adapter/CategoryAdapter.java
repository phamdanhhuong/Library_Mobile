package com.phamhuong.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.MainActivity;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private List<Category> categoryList;
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtGenre.setText(category.getGenre());

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(category.getImages())
                .into(holder.imgCategory);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).fetchBookByCategory(category.getGenre());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtGenre;
        ImageView imgCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.tvCategoryName);
            imgCategory = itemView.findViewById(R.id.categoryImage);
        }
    }
}
