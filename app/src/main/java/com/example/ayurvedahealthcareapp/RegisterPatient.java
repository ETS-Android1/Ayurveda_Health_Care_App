package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterPatient extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText pFullName,pEmail,pPassword,pPhone,pBirthDate;
    DatePickerDialog picker;
    Button pRegisterBtn;
    TextView pLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        //initialize data
        pFullName       = findViewById(R.id.fullName);
        pEmail          = findViewById(R.id.Email);
        pPassword       = findViewById(R.id.password);
        pPhone          = findViewById(R.id.phone);
        pRegisterBtn    = findViewById(R.id.registerButton);
        pLoginBtn       = findViewById(R.id.createText);
        pBirthDate      = findViewById(R.id.birthDate);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //if the user is logged in, direct them to the homepage
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

        //Get calender for birthdate
        pBirthDate.setInputType(InputType.TYPE_NULL);
        pBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterPatient.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                pBirthDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        pRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = pEmail.getText().toString().trim();
                String password = pPassword.getText().toString().trim();
                String fullName = pFullName.getText().toString();
                String phone = pPhone.getText().toString();
                String birthdate = pBirthDate.getText().toString();

                if(TextUtils.isEmpty(fullName)){
                    pFullName.setError("Enter your full name!");
                }
                if(TextUtils.isEmpty(email)){
                    pEmail.setError(("Email is required. "));
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    pPassword.setError("Password must be at least 6 characters long!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(RegisterPatient.this, "User created successfully", Toast.LENGTH_SHORT).show();

                            //Storing user data in firebase
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("birthDate",birthdate);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("isPatient","1");
                            user.put("pUrl","https://firebasestorage.googleapis.com/v0/b/ayurveda-health-care-app.appspot.com/o/profile.png?alt=media&token=02b49adf-c9b7-4500-83ff-562177cec567");

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for patient " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Dashboard.class));

                        }
                        else{
                            Toast.makeText(RegisterPatient.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        pLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}