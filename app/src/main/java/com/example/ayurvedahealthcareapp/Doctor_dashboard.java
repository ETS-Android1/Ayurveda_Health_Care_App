package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_dashboard extends AppCompatActivity  {

    CardView doctor_me;
    /*CircleImageView profileImage;
    FirebaseAuth fAuth;
    StorageReference storageReference;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        doctor_me=(CardView) findViewById(R.id.d_me);
       /* profileImage = findViewById(R.id.profilePic);

        fAuth       = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //load profile picture
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });*/


        doctor_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


}