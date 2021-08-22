package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Dashboard extends AppCompatActivity {

    CardView location,calender,doctor,medicine,home_remedies,pharmacy;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        location=findViewById(R.id.locid);
        pharmacy=findViewById(R.id.phar);
        doctor=findViewById(R.id.doc);
        home_remedies=findViewById(R.id.hom);
        calender=findViewById(R.id.cal);
        medicine=findViewById(R.id.med);
        profile = findViewById(R.id.profilePicture);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        final Intent intent1=new Intent(this,location_Activity.class);
        String name="Vinod Piumantha ";
        intent1.putExtra("User_Name",name );
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent1 );
    }

});

        final Intent intent2=new Intent(this,doctor_Activity.class);
      //  String name="Vinod Piumantha ";
     //   intent2.putExtra("User_Name",name );
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent2 );
            }

        });



        final Intent intent3=new Intent(this,pharmacy_Activity.class);
       // String name="Vinod Piumantha ";
       // intent3.putExtra("User_Name",name );
        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent3 );
            }

        });



        final Intent intent4=new Intent(this,calender_Activity.class);
       // String name="Vinod Piumantha ";
       // intent4.putExtra("User_Name",name );
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent4 );
            }

        });



        final Intent intent5=new Intent(this,home_remidies_Activity.class);
      //  String name="Vinod Piumantha ";
       // intent5.putExtra("User_Name",name );
        home_remedies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent5 );
            }

        });


        final Intent intent6=new Intent(this,medicine_Activity.class);
       // String name="Vinod Piumantha ";
       // intent6.putExtra("User_Name",name );
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent6 );
            }

        });

    }
}