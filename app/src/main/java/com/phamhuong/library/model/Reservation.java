package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {

    @SerializedName("reservationId")
    private Integer reservationId;

    @SerializedName("user")
    private User user;

    @SerializedName("reservation_date")
    private LocalDateTime reservationDate;

    @SerializedName("expiration_date")
    private LocalDateTime expirationDate;

    @SerializedName("status")
    private String status;

    @SerializedName("reservation_books")
    private List<ReservationBook> reservationBooks;

    // Getters and Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ReservationBook> getReservationBooks() {
        return reservationBooks;
    }

    public void setReservationBooks(List<ReservationBook> reservationBooks) {
        this.reservationBooks = reservationBooks;
    }

    public int getBookCount() {
        if (reservationBooks == null) return 0;
        return reservationBooks.size();
    }
}