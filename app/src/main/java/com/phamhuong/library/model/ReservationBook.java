package com.phamhuong.library.model;

import com.google.gson.annotations.SerializedName;

public class ReservationBook {

    @SerializedName("id")
    private Integer id;
    @SerializedName("reservation_id")
    private Reservation reservation;
    @SerializedName("book_id")
    private Book book;

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}