package com.example.ayurvedahealthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FinishAppointment extends BottomSheetDialogFragment {

    TextView patientName,patientPhone,appointmentTime,appointmentDate;
    public static final String TAG = "ActionBottomDialog";
    Button finishAppointment;


    public static FinishAppointment newInstance() {
        return new FinishAppointment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finish_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientName = view.findViewById(R.id.finish_Appointment_fullName);
        patientPhone =  view.findViewById(R.id.finish_Appointment_phone);
        appointmentTime = view.findViewById(R.id.finish_Appointment_time);
        appointmentDate = view.findViewById(R.id.finish_Appointment_date);
        finishAppointment = view.findViewById(R.id.finishAppointment);

        patientName.setText(CommonValues.currentBookingInformation.getPatientName());
        patientPhone.setText(CommonValues.currentBookingInformation.getPatientPhone());
        appointmentDate.setText(CommonValues.convertTimeStampToStringKey(CommonValues.currentBookingInformation.getTimestamp()));
        appointmentTime.setText(CommonValues.convertTimeSlotToString((int) CommonValues.currentBookingInformation.getSlot()));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

        finishAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //appointment done, making done=true

                DocumentReference bookingSet = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection(CommonValues.simpleDateFormat.format(CommonValues.selectedDate.getTime()))
                        .document(CommonValues.currentBookingInformation.getBookingId());

                bookingSet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            if(task.getResult().exists())
                            {
                                Map<String,Object> dataUpdate = new HashMap<>();
                                dataUpdate.put("done",true);
                                bookingSet.update(dataUpdate).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            CollectionReference userBooking = FirebaseFirestore.getInstance().collection("Patients")
                                                    .document(CommonValues.currentBookingInformation.getPatientPhone())
                                                    .collection("Booking");

                                            userBooking.whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                                                    .whereEqualTo("done",false)
                                                    .limit(1).get()
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if(task.isSuccessful()){
                                                        if(task.getResult().size() > 0){
                                                            DocumentReference bookUser =null;
                                                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                                                bookUser = userBooking.document(documentSnapshot.getId());
                                                            }
                                                            if(bookUser != null){
                                                                Map<String,Object> updateData = new HashMap<>();
                                                                updateData.put("done",true);
                                                                bookUser.update(updateData).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getContext(),"Appointment is Done!! ", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(getContext(), AppointmentDoctor.class));
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
