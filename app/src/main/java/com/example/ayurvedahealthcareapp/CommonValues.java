package com.example.ayurvedahealthcareapp;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonValues {

    public static Doctor currentDoctor;
    public static int currentTimeSlot = -1;
    public static BookingInformation currentBooking;
    public static String currentBookingId="";

    public static String convertTimeStampToStringKey(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(date);
    }
}
