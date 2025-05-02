package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Reservation implements Serializable {

    @SerializedName("reservationId")
    private Integer reservationId;

    @SerializedName("user")
    private User user;

    @SerializedName("reservationDate")
    private String reservationDate;

    @SerializedName("expirationDate")
    private String expirationDate;

    @SerializedName("status")
    private String status;

    @SerializedName("reservation_books")
    private List<ReservationBook> reservationBooks;
    @SerializedName("bookCount")
    private Integer bookCount;

    // Getters and Setters
    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }
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

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
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
}