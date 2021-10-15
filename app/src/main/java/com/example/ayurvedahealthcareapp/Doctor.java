package com.example.ayurvedahealthcareapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {

    private String fName,birthDate, email,phone, NIC,Registration_ID, pUrl,DoctorId;
    private Double rating;
    private Long ratingTimes;

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        fName = in.readString();
        birthDate = in.readString();
        email = in.readString();
        phone = in.readString();
        NIC = in.readString();
        Registration_ID = in.readString();
        pUrl = in.readString();
        DoctorId = in.readString();
        rating = in.readDouble();
        ratingTimes = in.readLong();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getRegistration_ID() {
        return Registration_ID;
    }

    public void setRegistration_ID(String registration_ID) {
        Registration_ID = registration_ID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(birthDate);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(NIC);
        dest.writeString(Registration_ID);
        dest.writeString(pUrl);
        dest.writeString(DoctorId);
        dest.writeDouble(rating);
        dest.writeLong(ratingTimes);

    }
}
