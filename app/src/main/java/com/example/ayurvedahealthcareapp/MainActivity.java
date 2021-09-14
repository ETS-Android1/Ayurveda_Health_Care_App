package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView fullName, email, phone, birthDate;
    Button changePassword, changeProPic;
    CircleImageView profileImage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    private Uri imageUri;

    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName    = findViewById(R.id.fullName);
        email       = findViewById(R.id.Email);
        phone       = findViewById(R.id.phone);
        birthDate   = findViewById(R.id.birthDate);
        changePassword  = findViewById(R.id.changePassword);
        changeProPic= findViewById(R.id.changeProPic);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userId      = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        profileImage = findViewById(R.id.profilePicture);


        // Fetch data from database and display

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    birthDate.setText(documentSnapshot.getString("birthDate"));

            }
        });



            //Change password
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View customLayout = getLayoutInflater().inflate(R.layout.change_password, null);
                    EditText cPassword = customLayout.findViewById(R.id.currentPassword);
                    EditText nPassword = customLayout.findViewById(R.id.newPassword);
                    Button updatePassword = customLayout.findViewById(R.id.updatePassword);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setView(customLayout);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    updatePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String oldPassword = cPassword.getText().toString().trim();
                            String newPassword = nPassword.getText().toString().trim();

                            if(TextUtils.isEmpty(oldPassword)){
                                Toast.makeText(MainActivity.this, "Please Enter your current password!", Toast.LENGTH_SHORT).show();
                            }
                            if(newPassword.length()<6){
                                Toast.makeText(MainActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                            updatePassword(oldPassword,newPassword);
                        }
                    });
                }
            });


            //Change profile picture
            changeProPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropImage.activity().setAspectRatio(1,1).start(MainActivity.this);
                }
            });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImage.setImageURI(imageUri);

            uploadImageToFirebase(imageUri);
        }
        else{
            Toast.makeText(MainActivity.this,"Error!!, Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload the image to the firebase

        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"Profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {

        FirebaseUser user = fAuth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Old password is correct
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Successfully changed the password
                        Toast.makeText(MainActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error!!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error!!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

}