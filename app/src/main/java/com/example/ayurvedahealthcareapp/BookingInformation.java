package com.example.ayurvedahealthcareapp;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private String patientName,patientPhone,time,doctorId,doctorName;
    private long slot;
    private Timestamp timestamp;
    private boolean done;

    public BookingInformation() {
    }

    public BookingInformation(String patientName, String patientPhone, String time, String doctorId, String doctorName, long slot) {
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.time = time;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.slot = slot;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
