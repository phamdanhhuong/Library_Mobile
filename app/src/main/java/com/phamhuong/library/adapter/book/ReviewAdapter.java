package com.phamhuong.library.adapter.book;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.Review;
import com.phamhuong.library.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        User user = review.getUser();
        if (user != null) {
            holder.tvUserName.setText(user.getFullName());
            // Load avatar nếu có
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                Glide.with(context)
                        .load(user.getAvatar())
                        .placeholder(R.drawable.avatar1) // Placeholder
                        .error(R.drawable.avatar1)     // Error
                        .into(holder.imgUserAvatar);
            } else {
                holder.imgUserAvatar.setImageResource(R.drawable.avatar1); // Default avatar
            }
        } else {
            holder.tvUserName.setText("Người dùng ẩn");
            holder.imgUserAvatar.setImageResource(R.drawable.avatar1);
        }
//        holder.ratingBar.setRating(review.getRating());
        holder.ratingBar.setRating((float) review.getRating());
        holder.tvCommentText.setText(review.getComment());
        if (review.getCreatedAt() != null) {
            try {
                LocalDateTime createdAt = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createdAt = LocalDateTime.parse(review.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.tvCommentTime.setText(createdAt.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
                }
            } catch (Exception e) {
                holder.tvCommentTime.setText(review.getCreatedAt());
            }
        } else {
            holder.tvCommentTime.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserAvatar;
        TextView tvUserName, tvCommentTime, tvCommentText;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}