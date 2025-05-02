package com.phamhuong.library.adapter.reservation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phamhuong.library.R;
import com.phamhuong.library.model.BorrowingRecord;
import com.phamhuong.library.utils.DateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class BorrowHistoryAdapter extends RecyclerView.Adapter<BorrowHistoryAdapter.ViewHolder> {
    private List<BorrowingRecord> borrowingRecords;
    private Context context;
    private OnRenewButtonClickListener renewButtonClickListener;

    public interface OnRenewButtonClickListener {
        void onRenewButtonClick(BorrowingRecord record);
    }

    public BorrowHistoryAdapter(Context context, List<BorrowingRecord> borrowingRecords, OnRenewButtonClickListener listener) {
        this.context = context;
        this.borrowingRecords = borrowingRecords;
        this.renewButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrow_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowingRecord record = borrowingRecords.get(position);

        // Load book cover image
        Glide.with(context)
                .load(record.getBook().getCoverUrl())
                .placeholder(R.drawable.book_placeholder)
                .into(holder.bookCover);

        // Set book title
        holder.bookTitle.setText(record.getBook().getTitle());

        // Set borrow date
        holder.borrowDate.setText(DateFormatter.formatDate(record.getBorrowDate()));

        // Set due date
        holder.dueDate.setText(DateFormatter.formatDate(record.getDueDate()));

        // Calculate remaining days
        if (record.getDueDate() != null && !"Returned".equals(record.getStatus())) {
            try {
                LocalDateTime dueDate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dueDate = LocalDateTime.parse(record.getDueDate(), DateFormatter.getApiDateTimeFormatter());
                }
                LocalDateTime now = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    now = LocalDateTime.now();
                }
                long remainingDays = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    remainingDays = ChronoUnit.DAYS.between(now.toLocalDate(), dueDate.toLocalDate());
                }

                if (remainingDays >= 0) {
                    holder.remainingDays.setText(String.valueOf(remainingDays) + " ngày còn lại");
                    holder.remainingDays.setTextColor(ContextCompat.getColor(context, R.color.status_completed));
                } else {
                    holder.remainingDays.setText(String.valueOf(Math.abs(remainingDays)) + " ngày quá hạn");
                    holder.remainingDays.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled));
                }
                holder.remainingDays.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                holder.remainingDays.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            holder.remainingDays.setVisibility(View.GONE);
        }

        // Set status
        holder.status.setText(record.getStatus());
        int statusColor;
        switch (record.getStatus()) {
            case "Returned":
                statusColor = R.color.status_completed;
                break;
            case "Overdue":
                statusColor = R.color.status_cancelled;
                break;
            default:
                statusColor = R.color.status_pending;
                break;
        }
        holder.status.setTextColor(ContextCompat.getColor(context, statusColor));

        // Display penalty fee if it's greater than 0
        if (record.getPenaltyFee() != null && record.getPenaltyFee() > 0) {
            holder.penaltyFee.setText(String.format(Locale.getDefault(), "Phí phạt: %.2f", record.getPenaltyFee()));
            holder.penaltyFee.setVisibility(View.VISIBLE);
        } else {
            holder.penaltyFee.setVisibility(View.GONE);
        }

        // Set OnClickListener for the Renew button
        holder.btnRenew.setOnClickListener(v -> {
            if (renewButtonClickListener != null) {
                renewButtonClickListener.onRenewButtonClick(record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return borrowingRecords != null ? borrowingRecords.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<BorrowingRecord> newRecords) {
        this.borrowingRecords = newRecords;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView borrowDate;
        TextView dueDate;
        TextView status;
        TextView remainingDays;
        Button btnRenew;
        TextView penaltyFee; // Add TextView for penalty fee

        ViewHolder(View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            borrowDate = itemView.findViewById(R.id.borrowDate);
            dueDate = itemView.findViewById(R.id.dueDate);
            status = itemView.findViewById(R.id.status);
            remainingDays = itemView.findViewById(R.id.remainingDays);
            btnRenew = itemView.findViewById(R.id.btnRenew);
            penaltyFee = itemView.findViewById(R.id.penaltyFee); // Initialize penaltyFee TextView
        }
    }

    public void setOnRenewButtonClickListener(OnRenewButtonClickListener listener) {
        this.renewButtonClickListener = listener;
    }
}