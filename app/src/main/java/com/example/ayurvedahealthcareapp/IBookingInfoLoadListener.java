package com.example.ayurvedahealthcareapp;

public interface IBookingInfoLoadListener {

    void onBookingInfoLoadEmpty();
    void onBookingInfoLoadSuccess(BookingInformation bookingInformation,String documentId);
    void  onBookingInfoLoadFailed(String msg);
}
