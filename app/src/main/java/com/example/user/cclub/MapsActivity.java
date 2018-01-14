package com.example.user.cclub;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SupportMapFragment mapFragment;
    public GoogleMap map;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //Menu references
    MenuHandler menuHandler;
    int menuCurrentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMap);
        mActivityTitle = getTitle().toString();

        menuCurrentID = R.id.map_page;
        menuHandler = new MenuHandler(this,menuCurrentID);

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

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

    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if( getSupportActionBar() !=null ) {
                    getSupportActionBar().setTitle("Navigation");
                }
            }

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menuHandler.onNavigationItemSelected(item);
        return false;
    }

}




