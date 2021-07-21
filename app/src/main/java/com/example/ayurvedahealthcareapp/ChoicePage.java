package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoicePage extends AppCompatActivity {

    Button doctorRegBtn,patientRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_page);

        doctorRegBtn    = findViewById(R.id.doctorBtn);
        patientRegBtn   = findViewById(R.id.patientBtn);

        doctorRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterDoctor.class));
            }
        });

        patientRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterPatient.class));
            }
        });
    }
}