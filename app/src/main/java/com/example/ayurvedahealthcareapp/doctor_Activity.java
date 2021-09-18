package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class doctor_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DoctorAdapter doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_);

        fAuth           = FirebaseAuth.getInstance();
        fStore          = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_doctor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(FirebaseFirestore.getInstance().collection("Users").whereEqualTo("isDoctor","1"), Doctor.class)
                .build();

        doctorAdapter = new DoctorAdapter(options);
        recyclerView.setAdapter(doctorAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        doctorAdapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        doctorAdapter.stopListening();
    }
}