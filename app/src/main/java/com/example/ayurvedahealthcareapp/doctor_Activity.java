package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class doctor_Activity extends AppCompatActivity {

    private static final String TAG ="TAG" ;
    LocalBroadcastManager localBroadcastManager;

    private RecyclerView recyclerView;

    DoctorAdapter doctorAdapter;
    CollectionReference doctorRef;

    ArrayList<Doctor> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        recyclerView = findViewById(R.id.recycler_doctor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorRef = FirebaseFirestore.getInstance().collection("Users");
        doctors= new ArrayList<>();

        //get the doctors where isDoctor = 1
        doctorRef.whereEqualTo("isDoctor","1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot doctorSnapshot:task.getResult()){
                    Doctor doctor = doctorSnapshot.toObject(Doctor.class);
                    doctor.setDoctorId(doctorSnapshot.getId());
                    //add doctor to the list
                    doctors.add(doctor);
                }
                doctorAdapter.notifyDataSetChanged();
                Intent intent = new Intent("Doctor Loading finished");
                intent.putParcelableArrayListExtra("Doctor Loading finished",doctors);
                localBroadcastManager.sendBroadcast(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
        // send the details to the adpter class
        doctorAdapter = new DoctorAdapter(this,doctors);
        recyclerView.setAdapter(doctorAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("newText1",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("newText",newText);
                doctorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


}