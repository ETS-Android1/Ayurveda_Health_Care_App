package com.example.ayurvedahealthcareapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView fullName, email, phone, birthDate;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName    = findViewById(R.id.fullName);
        email       = findViewById(R.id.Email);
        phone       = findViewById(R.id.phone);
        birthDate   = findViewById(R.id.birthDate);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userId      = fAuth.getCurrentUser().getUid();

        // Fetch data from database and display

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        if(documentReference != null) {
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    phone.setText(value.getString("phone"));
                    fullName.setText(value.getString("fName"));
                    email.setText(value.getString("email"));
                    birthDate.setText(value.getString("birthDate"));
                }
            });
        }
        else {
            DocumentReference documentReference2 = fStore.collection("Doctors").document(userId);
            documentReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    phone.setText(value.getString("phone"));
                    fullName.setText(value.getString("fName"));
                    email.setText(value.getString("email"));
                    birthDate.setText(value.getString("birthDate"));
                }
            });
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void P1_bay(View view) {
    }
}