package com.example.ayurvedahealthcareapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FinishAppointment extends BottomSheetDialogFragment {

    TextView patientName,patientPhone,appointmentTime,appointmentDate;
    public static final String TAG = "ActionBottomDialog";

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

        patientName.setText(CommonValues.currentBookingInformation.getPatientName());
        patientPhone.setText(CommonValues.currentBookingInformation.getPatientPhone());
        appointmentDate.setText(CommonValues.convertTimeStampToStringKey(CommonValues.currentBookingInformation.getTimestamp()));
        appointmentTime.setText(CommonValues.convertTimeSlotToString((int) CommonValues.currentBookingInformation.getSlot()));
    }
}
