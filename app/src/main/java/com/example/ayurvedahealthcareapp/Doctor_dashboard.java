package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Doctor_dashboard extends AppCompatActivity implements View.OnClickListener {
    CardView doctor_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        doctor_me=(CardView) findViewById(R.id.d_me);


        doctor_me.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch ( (v.getId()))
        {
            case R.id.d_me : i=new Intent(this,Doctor_Me.class);break;
        }
    }
}