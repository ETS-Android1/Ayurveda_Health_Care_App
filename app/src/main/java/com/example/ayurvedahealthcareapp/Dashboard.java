package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    CardView location,calender,doctor,medicine,home_remedies,pharmacy;
    CircleImageView profileImage;
    FirebaseAuth fAuth;
    StorageReference storageReference;

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
        profileImage = findViewById(R.id.profilePicture);

        fAuth       = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        //load profile picture
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
        // go to profile
        profileImage.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        checkRatingDialog();
    }

    private void checkRatingDialog() {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String patientPhone = documentSnapshot.getString("phone");

                CollectionReference userBooking = FirebaseFirestore.getInstance().collection("Patients")
                        .document(patientPhone)
                        .collection("Booking");

                userBooking.whereEqualTo("done",true).whereEqualTo("ratedDoctor",false).limit(1)
                        .get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task0) {
                        if (task0.isSuccessful()) {
                            if (!task0.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task0.getResult()) {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    break;
                                }

                                DocumentReference documentReferenceDoc = FirebaseFirestore.getInstance().collection("Users")
                                        .document(CommonValues.currentBookingInformation.getDoctorId());

                                documentReferenceDoc.get().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Doctor doctorRate = task.getResult().toObject(Doctor.class);
                                        doctorRate.setDoctorId(task.getResult().getId());

                                        View view = LayoutInflater.from(Dashboard.this).inflate(R.layout.layout_rating_dialog,null);

                                        TextView txt_doctor_name = (TextView)view.findViewById(R.id.rating_doctor_name);
                                        AppCompatRatingBar ratingBar = (AppCompatRatingBar)view.findViewById(R.id.rating);
                                        
                                        txt_doctor_name.setText(doctorRate.getfName());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this)
                                                .setView(view).setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Double originalRating = doctorRate.getRating();
                                                        Long ratingTimes = doctorRate.getRatingTimes();
                                                        float userRating = ratingBar.getRating();

                                                        Double finalRating = (originalRating+userRating);

                                                        Map<String,Object> dataUpdate = new HashMap<>();
                                                        dataUpdate.put("rating",finalRating);
                                                        dataUpdate.put("ratingTimes",++ratingTimes);

                                                        documentReferenceDoc.update(dataUpdate).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task1) {
                                                                if(task1.isSuccessful()){


                                                                    if(task0.getResult().size() > 0){
                                                                        DocumentReference rateUser =null;
                                                                        for(DocumentSnapshot documentSnapshot:task0.getResult()){
                                                                            rateUser = userBooking.document(documentSnapshot.getId());
                                                                        }
                                                                        if(rateUser != null){
                                                                            Map<String,Object> ratingDataUpdate = new HashMap<>();
                                                                            ratingDataUpdate.put("ratedDoctor",true);
                                                                            rateUser.update(ratingDataUpdate).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    Toast.makeText(Dashboard.this,"Thank you for rating!", Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }).setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).setNeutralButton("NEVER", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (task0.getResult().size() > 0) {
                                                            DocumentReference rateUser = null;
                                                            for (DocumentSnapshot documentSnapshot : task0.getResult()) {
                                                                rateUser = userBooking.document(documentSnapshot.getId());
                                                            }
                                                            if (rateUser != null) {
                                                                Map<String, Object> ratingDataUpdate = new HashMap<>();
                                                                ratingDataUpdate.put("ratedDoctor", true);
                                                                rateUser.update(ratingDataUpdate).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(Dashboard.this, "Thank you!", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }
                                                });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });

                            }
                        }
                    }
                });
            }
        });
    }
}