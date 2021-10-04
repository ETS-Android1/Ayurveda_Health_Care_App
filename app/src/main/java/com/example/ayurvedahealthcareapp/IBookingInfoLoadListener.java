package com.example.ayurvedahealthcareapp;

public interface IBookingInfoLoadListener {

    void onBookingInfoLoadEmpty();
    void onBookingInfoLoadSuccess(BookingInformation bookingInformation);
    void  onBookingInfoLoadFailed(String msg);
}
