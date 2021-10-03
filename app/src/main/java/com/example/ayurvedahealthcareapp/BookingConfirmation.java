package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingConfirmation extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;
    TextView drName, bookTime, bookDate;
    Button okay,cancel;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId,patientName,patientPhone;

    public static int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        time = (int) getIntent().getExtras().get("Time Slot");

        drName = findViewById(R.id.drName);
        bookTime = findViewById(R.id.booked_time);
        bookDate = findViewById(R.id.booked_date);

        okay = findViewById(R.id.okay);
        cancel  = findViewById(R.id.cancel);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        userId      = fAuth.getCurrentUser().getUid();

        //set the texts

        drName.setText(Booking.receiveDoctorName);
        bookDate.setText(simpleDateFormat.format(Booking.selected_date.getTime()));
        bookTime.setText(convertTimeSlotToString(time));



        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                patientName = documentSnapshot.getString("fName");
                patientPhone = documentSnapshot.getString("phone");


            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingInformation bookingInformation = new BookingInformation();

                bookingInformation.setDoctorId(Booking.receiveDoctorID);
                bookingInformation.setDoctorName(Booking.receiveDoctorName);
                bookingInformation.setPatientName(patientName);
                bookingInformation.setPatientPhone(patientPhone);
                bookingInformation.setSlot(Long.valueOf(time));
                bookingInformation.setTime(simpleDateFormat.format(Booking.selected_date.getTime()));

                DocumentReference bookingDate = fStore.collection("Users").document(Booking.receiveDoctorID)
                        .collection(new SimpleDateFormat("dd_MM_yyyy").format(Booking.selected_date.getTime()))
                        .document(String.valueOf(time));

                bookingDate.set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resetStaticData();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        Toast.makeText(BookingConfirmation.this, "Appointment is Successful!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookingConfirmation.this, "Failed!!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        });

    }

    private void resetStaticData() {
        Booking.receiveDoctorName = null;
        Booking.receiveDoctorID = null;
        time = -1;
        Booking.selected_date.add(Calendar.DATE,0);
    }

    private String convertTimeSlotToString(int slot) {
        switch (slot){
            case 0:
                return "9.30 - 9.45";
            case 1:
                return "9.45 - 10.00";
            case 2:
                return "10.00 - 10.15";
            case 3:
                return "10.15 - 10.30";
            case 4:
                return "10.30 - 10.45";
            case 5:
                return "10.45 - 11.00";
            case 6:
                return "11.00 - 11.15";
            case 7:
                return "11.15 - 11.30";
            case 8:
                return "11.30 - 11.45";
            case 9:
                return "11.45 - 12.00";
            case 10:
                return "12.30 - 12.45";
            case 11:
                return "12.45 - 13.00";
            case 12:
                return "13.00 - 13.15";
            case 13:
                return "13.15 - 13.30";
            case 14:
                return "13.30 - 13.45";
            case 15:
                return "13.45 - 14.00";
            case 16:
                return "14.00 - 14.15";
            case 17:
                return "14.15 - 14.30";
            case 18:
                return "14.30 - 14.45";
            case 19:
                return "14.45 - 15.00";
            case 20:
                return "15.00 - 15.15";
            case 21:
                return "15.15 - 15.30";
            case 22:
                return "15.30 - 15.45";
            case 23:
                return "15.45 - 16.00";
            case 24:
                return "16.00 - 16.15";
            case 25:
                return "16.15 - 16.30";
            case 26:
                return "16.30 - 16.45";
            case 27:
                return "16.45 - 17.00";
            case 28:
                return "17.00 - 17.15";
            case 29:
                return "17.15 - 17.30";
            case 30:
                return "17.30 - 17.45";
            case 31:
                return "17.45 - 18.00";
            case 32:
                return "18.00 - 18.15";
            case 33:
                return "18.15 - 18.30";
            case 34:
                return "18.30 - 18.45";
            case 35:
                return "18.45 - 19.00";
            case 36:
                return "19.00 - 19.15";
            case 37:
                return "19.15 - 19.30";
            case 38:
                return "19.30 - 19.45";
            case 39:
                return "19.45 - 20.00";
            default:
                return "Closed";

        }
    }
}