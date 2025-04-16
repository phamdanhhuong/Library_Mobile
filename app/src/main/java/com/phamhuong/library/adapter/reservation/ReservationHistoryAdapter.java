package com.phamhuong.library.adapter.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Reservation;

import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.tvReservationId.setText("Mã đặt sách: #" + reservation.getReservationId());
        holder.tvReservationDate.setText("Ngày đặt: " + reservation.getReservationDate());
        holder.tvBookCount.setText("Số lượng sách: " + reservation.getBookCount());
        holder.tvStatus.setText(reservation.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReservationClick(reservation);
            }
        });
    }
    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReservationId, tvReservationDate, tvBookCount, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationId = itemView.findViewById(R.id.tvReservationId);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvBookCount = itemView.findViewById(R.id.tvBookCount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
