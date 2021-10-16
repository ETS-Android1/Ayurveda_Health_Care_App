package com.example.ayurvedahealthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class location_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap pMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    List<LocationHelper> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Patient_Google_map);
        searchView = findViewById(R.id.search_dcotor_location);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(location_Activity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    pMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        pMap = googleMap;

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        locationList = new ArrayList<>();

        CollectionReference doctorLocations = fStore.collection("Locations");

        doctorLocations.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot.isEmpty()){
                        Toast.makeText(location_Activity.this,"Empty Locations!!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        for(QueryDocumentSnapshot document:task.getResult()){
                            locationList.add(document.toObject(LocationHelper.class));

                        }

                        for(int i =0; i<locationList.size(); i++){
                            LatLng latLng = new LatLng(locationList.get(i).getLatitude(),locationList.get(i).getLongitude());

                            //retrieve doctor information
                            DocumentReference documentReference = fStore.collection("Users")
                                    .document(locationList.get(i).getDoctorId());

                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String docName = documentSnapshot.getString("fName");
                                    String docPhone = documentSnapshot.getString("phone");

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title(docName);
                                    markerOptions.snippet(docPhone);
                                    pMap.addMarker(markerOptions);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(location_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(location_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}