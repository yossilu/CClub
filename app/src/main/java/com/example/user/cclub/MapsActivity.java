package com.example.user.cclub;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


    public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_location_page);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapViewContact);
            mapFragment.getMapAsync(this);

            Button gotoRead = (Button) findViewById(R.id.gotoBtnLocation);
            gotoRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this, ReadmePage.class);
                    startActivity(intent);
                }
            });
        }




        /**
         * will call the map when available.
         * possible to add extra methods such as add lines for example.
         * supportMapFragment object supports google play services
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng holon = new LatLng(32.019088, 34.76922969999998);
            //move camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(holon));
            //marker to Holon Israel Ehad be'mai
            mMap.addMarker(new MarkerOptions().position(holon).title("Marker in Israel, Holon, Ehad Be'Mai"));


        }
    }

