package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Booking extends AppCompatActivity implements ITimeSlotListener {

    DocumentReference doctorDoc;
    ITimeSlotListener iTimeSlotListener;

    LocalBroadcastManager localBroadcastManager;
    private RecyclerView recyclerView;

    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
    public static Calendar selected_date;

    public static String receiveDoctorID;
    public static String receiveDoctorName;



    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotOfDoctor("Doctor Selected",simpleDateFormat.format(date.getTime()));

        }
    };

    private void loadAvailableTimeSlotOfDoctor(String doctorId, final String bookDate) {

        receiveDoctorID = getIntent().getExtras().get("Doctor Selected").toString();
        receiveDoctorName = getIntent().getExtras().get("Doctor Name").toString();

        doctorDoc = FirebaseFirestore.getInstance().collection("Users").document(CommonValues.currentDoctor.getDoctorId());

        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){

                        CollectionReference date = FirebaseFirestore.getInstance().collection("Users").document(CommonValues.currentDoctor.getDoctorId())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty()){
                                        iTimeSlotListener.onTimeSlotLoadEmpty();
                                    }
                                    else {
                                        // If we have appointments
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document:task.getResult()){
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        }
                                        iTimeSlotListener.onTimeSlotLoadSuccess(timeSlots);

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.recycler_time_slot);
        calendarView = findViewById(R.id.calendar_view);

        iTimeSlotListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter("Display Time Slot"));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,3);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendar_view)
                .range(startDate,endDate).datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate).build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(selected_date.getTimeInMillis() != date.getTimeInMillis()){
                    selected_date = date;
                    loadAvailableTimeSlotOfDoctor("Doctor Selected",simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(Booking.this,timeSlotList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTimeSlotLoadFailed(String msg) {
        Toast.makeText(Booking.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSlotLoadEmpty() {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(Booking.this);
        recyclerView.setAdapter(adapter);

    }
}