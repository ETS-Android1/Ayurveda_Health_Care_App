package com.example.ayurvedahealthcareapp;

import java.util.List;

public interface IDoctorTimeSlotListener {
    void onTimeSlotLoadSuccess(List<BookingInformation> timeSlotList);
    void onTimeSlotLoadFailed(String msg);
    void onTimeSlotLoadEmpty();
}
