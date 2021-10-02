package com.example.ayurvedahealthcareapp;

import java.util.List;

public interface ITimeSlotListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String msg);
    void onTimeSlotLoadEmpty();
}
