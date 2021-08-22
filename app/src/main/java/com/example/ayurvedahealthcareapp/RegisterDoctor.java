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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterDoctor extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText dFullName,dEmail,dPassword,dPhone,dNic,dRegId,dBirthDate;
    Button dRegisterBtn;
    TextView dLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String doctorID;


    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        //initialize data
        dFullName       = findViewById(R.id.fullName);
        dEmail          = findViewById(R.id.Email);
        dPassword       = findViewById(R.id.password);
        dPhone          = findViewById(R.id.phone);
        dNic            = findViewById(R.id.nic);
        dRegId          = findViewById(R.id.registrationId);
        dRegisterBtn    = findViewById(R.id.registerButton);
        dLoginBtn       = findViewById(R.id.createText);
        dBirthDate      = findViewById(R.id.birthDate);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

        //get birthdate
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterDoctor.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = dEmail.getText().toString().trim();
                String password = dPassword.getText().toString().trim();
                String fullName = dFullName.getText().toString();
                String nic  = dNic.getText().toString();
                String regId = dRegId.getText().toString();
                String birthdate = dBirthDate.getText().toString();
                String phone = dPhone.getText().toString();

                // Validation
                // If email,password,nic and registration number is empty
                if(TextUtils.isEmpty(fullName)){
                    dFullName.setError("Enter your full name!");
                }
                if(TextUtils.isEmpty(email)){
                    dEmail.setError(("Email is required! "));
                    return;
                }
                if(TextUtils.isEmpty(nic)){
                    dNic.setError("NIC number is required");
                    return;
                }
                if(nic.length() < 10){
                    dNic.setError("Enter a valid NIC number!");
                    return;
                }
                if(TextUtils.isEmpty(regId)){
                    dRegId.setError("Registration number is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    dPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 6){
                    dPassword.setError("Password must be at least 6 characters long!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterDoctor.this, "User created successfully", Toast.LENGTH_SHORT).show();

                            //Storing doctor information in the database
                            doctorID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Doctors").document(doctorID);
                            Map<String,Object> doctor = new HashMap<>();
                            doctor.put("fName",fullName);
                            doctor.put("birthDate",birthdate);
                            doctor.put("email",email);
                            doctor.put("phone",phone);
                            doctor.put("NIC",nic);
                            doctor.put("Registration_ID",regId);

                            documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: Profile is created for Doctor " + doctorID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }
                        else{
                            Toast.makeText(RegisterDoctor.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        dLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dBirthDate.setText(sdf.format(myCalendar.getTime()));
    }
}