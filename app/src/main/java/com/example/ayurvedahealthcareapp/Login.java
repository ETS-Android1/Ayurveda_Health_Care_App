package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText nEmail,nPassword;
    Button nLoginBtn;
    TextView nCreateBtn,forgotTextLink;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nEmail          = findViewById(R.id.Email);
        nPassword       = findViewById(R.id.password);
        nLoginBtn       = findViewById(R.id.loginButton);
        nCreateBtn      = findViewById(R.id.createText);
        fAuth           = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        progressBar     = findViewById(R.id.progressBarLogin);
        forgotTextLink  = findViewById(R.id.forgotPassword);


        nLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = nEmail.getText().toString().trim();
                String password = nPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    nEmail.setError(("Email is required. "));
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    nPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    nPassword.setError("Password must be at least 6 characters long!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Sign in to the app
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            try {
                                userId = fAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = fStore.collection("Users").document(userId);

                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.getString("isPatient") != null) {
                                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                            finish();
                                        }
                                        if (documentSnapshot.getString("isDoctor") != null) {
                                            startActivity(new Intent(getApplicationContext(), Doctor_dashboard.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                            catch (Exception e){
                                Toast.makeText(Login.this, "Error!!" + e, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(Login.this, "Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        nCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChoicePage.class));
            }
        });

        //reset password
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password? ");
                passwordResetDialog.setMessage("Enter your Email to receive reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error!! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //closes the popup
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }

    //If the user is already logged in direct them to the dashboard
    @Override
    protected void onStart() {
        super.onStart();

        if(fAuth.getInstance().getCurrentUser() !=null){
            try {
                userId = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("Users").document(userId);

                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("isPatient") != null) {
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        }
                        if (documentSnapshot.getString("isDoctor") != null) {
                            startActivity(new Intent(getApplicationContext(), Doctor_dashboard.class));
                            finish();
                        }
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(Login.this, "Error!!" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}