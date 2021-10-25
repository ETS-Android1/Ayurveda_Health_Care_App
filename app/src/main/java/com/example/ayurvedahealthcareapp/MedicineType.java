package com.example.ayurvedahealthcareapp;

public class MedicineType {


    // variables for storing our data.
    private String medicineName, medicineDescription, medicineDuration,type,origin,discription;

    public MedicineType() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public MedicineType(String medicineName, String medicineDescription, String medicineDuration,String discription, String origin,String type) {
        this.medicineName = medicineName;
        this.medicineDescription = medicineDescription;
        this.medicineDuration = medicineDuration;
        this.discription=discription;
        this.origin=origin;
        this.type=type;
    }

    // getter methods for all variables.
    public String getCourseName() {
        return medicineName;
    }

    public void setCourseName(String courseName) {
        this.medicineName = medicineName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCourseDescription() {
        return medicineDescription;
    }

    // setter method for all variables.
    public void setCourseDescription(String medicineDescription) {
        this.medicineDescription = medicineDescription;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getCourseDuration() {
        return medicineDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.medicineDuration = courseDuration;
    }
    public String getType(){return type;}
    public void setType(String type)
    {
        this.type=type;
    }
}
