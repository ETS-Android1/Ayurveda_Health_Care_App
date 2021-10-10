package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class AppointmentDoctor extends AppCompatActivity implements ITimeSlotListener {

    ITimeSlotListener iTimeSlotListener;
    DocumentReference doctorDoc;

    private RecyclerView recyclerView;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
    Calendar selected_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_doctor);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userId      = fAuth.getCurrentUser().getUid();

        iTimeSlotListener = this;
        recyclerView = findViewById(R.id.doctor_recycler_time_slot);
        calendarView = findViewById(R.id.doctor_calendar_view);

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        selected_date = Calendar.getInstance();

        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,0);
        loadAvailableTimeSlotOfDoctor(userId,simpleDateFormat.format(date.getTime()));

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,3);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.doctor_calendar_view)
                .range(startDate,endDate).datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate).build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(selected_date.getTimeInMillis() != date.getTimeInMillis()){
                    selected_date = date;
                    loadAvailableTimeSlotOfDoctor(userId,simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }

    private void loadAvailableTimeSlotOfDoctor(String userId, String bookDate) {

        doctorDoc = fStore.collection("Users").document(userId);

        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){

                        CollectionReference date = FirebaseFirestore.getInstance().collection("Users").document(userId)
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
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

        MyDoctorTimeSlotAdapter adapter = new MyDoctorTimeSlotAdapter(AppointmentDoctor.this,timeSlotList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTimeSlotLoadFailed(String msg) {

        Toast.makeText(AppointmentDoctor.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyDoctorTimeSlotAdapter adapter = new MyDoctorTimeSlotAdapter(AppointmentDoctor.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Doctor_dashboard.class));
        finish();
    }
}