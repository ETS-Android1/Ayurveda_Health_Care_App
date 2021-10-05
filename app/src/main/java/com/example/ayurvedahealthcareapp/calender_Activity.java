package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    Button deleteAppointment;

    FirebaseFirestore fStore;
    public static String patientPhone;


    IBookingInfoLoadListener iBookingInfoLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_);

        card_booking_info = findViewById(R.id.card_booking_info);
        bookDr = findViewById(R.id.calendar_drName);
        bookDrTime = findViewById(R.id.calendar_booked_time);
        deleteAppointment = findViewById(R.id.calendar_delete_booking);

        timeRemaining = findViewById(R.id.calendar_remaining_time);
        nothing = findViewById(R.id.textnothing);

        iBookingInfoLoadListener = this;

        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                patientPhone = documentSnapshot.getString("phone");


            //loading user bookings
                CollectionReference userBooking = fStore.collection("Patients")
                        .document(patientPhone).collection("Booking");

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);

                Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

                //selecting info where done = false and timestamp greater than today
                userBooking.whereGreaterThan("timestamp", toDayTimeStamp).whereEqualTo("done", false)
                            .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                        iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation,queryDocumentSnapshot.getId());
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
        });

        deleteAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonValues.currentBooking!=null){

                    //Delete the booking from doctor

                    DocumentReference doctorBookingInfo = fStore.collection("Users")
                            .document(CommonValues.currentBooking.getDoctorId())
                            .collection(CommonValues.convertTimeStampToStringKey(CommonValues.currentBooking.getTimestamp()))
                            .document(String.valueOf(CommonValues.currentBooking.getSlot()));

                    doctorBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(calender_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            deleteBookingFromUser();
                        }
                    });


                }
                else{
                    Toast.makeText(calender_Activity.this, "You don't have a booking!!! " +
                            "Current booking is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteBookingFromUser() {
        if(!TextUtils.isEmpty(CommonValues.currentBookingId)){

            DocumentReference userBookingInfo = fStore.collection("Patients")
                    .document(patientPhone).collection("Booking").document(CommonValues.currentBookingId);

            userBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(calender_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(calender_Activity.this);

                    View customLayout = getLayoutInflater().inflate(
                                    R.layout.layout_success_dialog, null);
                    builder.setView(customLayout);

                    final AlertDialog alertDialog = builder.create();

                    customLayout.findViewById(R.id.buttonOkay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        }
                    });
                    if (alertDialog.getWindow() != null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }
            });

        }

    }

    @Override
    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
        nothing.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation,String bookingId) {

        CommonValues.currentBooking = bookingInformation;
        CommonValues.currentBookingId = bookingId;

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