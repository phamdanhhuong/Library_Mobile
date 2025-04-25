package com.phamhuong.library.adapter.reservation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;
import com.google.android.material.chip.Chip;
import com.google.android.material.button.MaterialButton;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.ViewHolder> {
    private Context context;
    private List<Reservation> reservations;
    private OnReservationClickListener listener;

    public interface OnReservationClickListener {
        void onReservationClick(Reservation reservation);
    }

    public ReservationHistoryAdapter(Context context, List<Reservation> reservations, OnReservationClickListener listener) {
        this.context = context;
        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.tvReservationId.setText("Đơn đặt sách #" + reservation.getReservationId());
        if (reservation.getReservationDate() != null) {
            holder.tvReservationDate.setText("Ngày đặt: " + formatDate(reservation.getReservationDate().toString()));
        } else {
            holder.tvReservationDate.setText("Ngày đặt: Không rõ ngày");
        }
        if (reservation.getExpirationDate() != null) {
            holder.tvExpirationDate.setText("Hạn trả sách: " + formatDate(reservation.getExpirationDate().toString()));
        } else {
            holder.tvExpirationDate.setText("Hạn trả sách: Không rõ ngày");
        }
        holder.tvBookCount.setText("Số lượng sách: " + reservation.getBookCount());

        Chip chipStatus = holder.chipStatus;
        chipStatus.setText(reservation.getStatus());

        int statusColor;
        switch (reservation.getStatus().toLowerCase()) {
            case "pending":
                statusColor = R.color.status_pending;
                break;
            case "confirmed":
                statusColor = R.color.status_confirmed;
                break;
            case "completed":
                statusColor = R.color.status_completed;
                break;
            case "cancelled":
                statusColor = R.color.status_cancelled;
                break;
            default:
                statusColor = R.color.status_pending;
            }
        
        chipStatus.setChipBackgroundColorResource(statusColor);
        chipStatus.setTextColor(ContextCompat.getColor(context, R.color.onPrimary));

        holder.btnViewDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReservationClick(reservation);
    }
        });
    }

    public String formatDate(String dateString) {
        try {
            if (dateString != null) {
                // Giả sử dateString là định dạng ISO 8601 như "2025-04-21T10:00:00Z"
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                Date date = originalFormat.parse(dateString);
            // Định dạng ngày tháng mà bạn muốn hiển thị
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return targetFormat.format(date);
            }
            else {
                return "Không rõ ngày";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;  // Trả về ngày gốc nếu có lỗi
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReservationId, tvReservationDate, tvExpirationDate, tvBookCount;
        Chip chipStatus;
        MaterialButton btnViewDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationId = itemView.findViewById(R.id.tvReservationId);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvExpirationDate = itemView.findViewById(R.id.tvExpirationDate);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
