package com.example.ayurvedahealthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_dashboard extends AppCompatActivity  {

    CardView doctor_me;
    ImageView back;
    /*CircleImageView profileImage;
    FirebaseAuth fAuth;
    StorageReference storageReference;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        doctor_me=(CardView) findViewById(R.id.d_me);
        back = findViewById(R.id.backImage);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(Doctor_dashboard.this);

                View customLayout = getLayoutInflater().inflate(
                        R.layout.layout_warning_dialog, null);
                builder.setView(customLayout);

                Button btn_no = (Button)customLayout.findViewById(R.id.buttonNo);
                Button btn_yes = (Button)customLayout.findViewById(R.id.buttonYes);

                final AlertDialog alertDialog = builder.create();

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        doctor_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}