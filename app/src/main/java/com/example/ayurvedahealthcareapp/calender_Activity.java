package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class calender_Activity extends AppCompatActivity implements IBookingInfoLoadListener {

    CardView card_booking_info;
    TextView bookDr,bookDrTime, timeRemaining,nothing;

    FirebaseFirestore fStore;

    IBookingInfoLoadListener iBookingInfoLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_);

        card_booking_info = findViewById(R.id.card_booking_info);
        bookDr = findViewById(R.id.calendar_drName);
        bookDrTime = findViewById(R.id.calendar_booked_time);

        timeRemaining = findViewById(R.id.calendar_remaining_time);
        nothing = findViewById(R.id.textnothing);

        iBookingInfoLoadListener = this;

        fStore = FirebaseFirestore.getInstance();


        //loading user bookings
        CollectionReference userBooking = fStore.collection("Patients")
                .document(BookingConfirmation.patientPhone).collection("Booking");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

        //selecting info where done = false and timestamp greater than today
        if (userBooking != null) {

            userBooking.whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp).whereEqualTo("done", false)
                    .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation);
                                break;
                            }
                        } else {
                            iBookingInfoLoadListener.onBookingInfoLoadEmpty();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
                }
            });
        }
        else {
            Toast.makeText(calender_Activity.this, "You have no Appointments!!! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
        nothing.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation) {

        bookDr.setText(bookingInformation.getDoctorName());
        bookDrTime.setText(bookingInformation.getTime());
        String dateRemaining = DateUtils.getRelativeTimeSpanString(Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(),0).toString();
        timeRemaining.setText(dateRemaining);

        card_booking_info.setVisibility(View.VISIBLE);
        nothing.setVisibility(View.GONE);

    }

    @Override
    public void onBookingInfoLoadFailed(String msg) {
        Toast.makeText(calender_Activity.this, msg, Toast.LENGTH_SHORT).show();
    }
}