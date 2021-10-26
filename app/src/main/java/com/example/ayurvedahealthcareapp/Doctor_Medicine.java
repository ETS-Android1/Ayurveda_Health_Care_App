package com.example.ayurvedahealthcareapp;



import android.content.Intent;
import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;

public class Doctor_Medicine extends AppCompatActivity {

    // creating variables for our edit text
    private EditText medicineNameEdt, medicineeDescriptionEdt, medicineDurationEdt,bigdisEdt;
    String items[]={"Tablet","Liquid","Cream","Plant","Other"};
    String items1[]={"Local","Imported"};
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    ArrayAdapter<String> adapterItems,adapterItems1;
    // creating variable for button
    private Button submitCourseBtn;

    // creating a strings for storing
    // our values from edittext fields.
    private String medicineName, medicineDuration, medicineDescription,userId,dicription;
    public static String typeId,typeId1;
    FirebaseAuth fAuth;

    // creating a variable
    // for firebasefirestore.
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__medicine);

        // getting our instance
        // from Firebase Firestore.
        fStore = FirebaseFirestore.getInstance();
        fAuth       = FirebaseAuth.getInstance();
        userId      = fAuth.getCurrentUser().getUid();

        // initializing our edittext and buttons
        medicineNameEdt = findViewById(R.id.idEdtMedicineName);
        medicineeDescriptionEdt = findViewById(R.id.idEdtMedicineDescription);
        medicineDurationEdt = findViewById(R.id.idEdtMedicineDuration);
        bigdisEdt=findViewById(R.id.desid);
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);


        autoCompleteTextView=findViewById(R.id.auto_Complete_TextView);
        autoCompleteTextView1=findViewById(R.id.auto_Complete_TextView1);

        adapterItems=new ArrayAdapter<>(this,R.layout.type_item_menu,items);
        autoCompleteTextView.setAdapter(adapterItems);

        adapterItems1=new ArrayAdapter<>(this,R.layout.type_item_menu,items1);
        autoCompleteTextView1.setAdapter(adapterItems1);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            typeId =parent.getItemAtPosition(position).toString();
                                                            Toast.makeText(getApplicationContext(),"item :"+typeId,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
        );

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            typeId1 =parent.getItemAtPosition(position).toString();
                                                            Toast.makeText(getApplicationContext(),"item :"+typeId1,Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
        );


        // adding on click listener for button
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                medicineName = medicineNameEdt.getText().toString();
                medicineDescription = medicineeDescriptionEdt.getText().toString();
                medicineDuration = medicineDurationEdt.getText().toString();
                dicription=bigdisEdt.getText().toString();
                String type;


                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(medicineName)) {
                    medicineNameEdt.setError("Please enter Medicine Name");
                } else if (TextUtils.isEmpty(medicineDescription)) {
                    medicineeDescriptionEdt.setError("Please enter Medicine Description");
                } else if (TextUtils.isEmpty(medicineDuration)) {
                    medicineDurationEdt.setError("Please enter Medicine amount");
                } else {
                    // calling method to add data to Firebase Firestore.
                    addDataToFirestore(medicineName, medicineDescription, medicineDuration,typeId1,typeId,dicription);
                }
            }
        });
    }


    private void addDataToFirestore( String medicineName, String medicineDescription, String medicineDuration, String dicription,String origin,String type) {

        // creating a collection reference
        // for our Firebase Firestore database.
       
        CollectionReference doctorDoc = fStore.collection("Users").document(userId).collection("Medicine");

        // adding our data to our courses object class.
        MedicineType medicine = new MedicineType(medicineName, medicineDescription, medicineDuration,dicription,origin, type);

        // below method is use to add data to Firebase Firestore.
        doctorDoc.add(medicine).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(Doctor_Medicine.this, "Your medicine has been added to the Database", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(Doctor_Medicine.this, "Fail to add medicine \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Doctor_dashboard.class));
        finish();
    }
}
