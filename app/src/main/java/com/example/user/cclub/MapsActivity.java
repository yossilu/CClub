package com.example.user.cclub;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    public GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.safety_map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Can't load Google map!!", Toast.LENGTH_SHORT).show();
        }
    }

        protected void loadMap(GoogleMap googleMap) {
            map = googleMap;
            if (map != null) {
                // Map is ready
                Toast.makeText(this, "Google Map was loaded properly!", Toast.LENGTH_SHORT).show();
                LatLng holon = new LatLng(32.019088, 34.76922969999998);
                //move camera
                map.moveCamera(CameraUpdateFactory.newLatLng(holon));
                //marker to Holon Israel Ehad be'mai
                map.moveCamera(CameraUpdateFactory.newLatLng(holon));
                map.animateCamera(CameraUpdateFactory.zoomTo(12));
                map.addMarker(new MarkerOptions().position(holon).title("Marker in Israel, Holon, Ehad Be'Mai"));
            } else {
                Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
            }
        }

    }


//
//        Button gotoRead = (Button) findViewById(R.id.gotoBtnLocation);
//        gotoRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MapsActivity.this, ReadmePage.class);
//                startActivity(intent);
//            }
//        });



