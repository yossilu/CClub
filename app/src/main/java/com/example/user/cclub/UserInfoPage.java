package com.example.user.cclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import Model.User;

/**
 * Created by talha on 14/01/2018.
 */

public class UserInfoPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    //Menu references
    User currentUser;
    MenuHandler menuHandler;
    int menuCurrentID;
    AutoCompleteTextView userPhone, userFirst, userLast, userAddress,userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        //initializing menu
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        menuCurrentID = R.id.info_page;
        menuHandler = new MenuHandler(this,menuCurrentID);
        currentUser = User.getCurrentUser();

        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPhone = (AutoCompleteTextView) findViewById(R.id.userPhone);
        userFirst = (AutoCompleteTextView) findViewById(R.id.userFirst);
        userLast = (AutoCompleteTextView) findViewById(R.id.userLast);
        userAddress = (AutoCompleteTextView) findViewById(R.id.userAddress);

        userEmail.setText(currentUser.getEmail());
        userPhone.setText(currentUser.getPhoneNumber());
        userFirst.setText(currentUser.getFirstName());
        userLast.setText(currentUser.getLastName());
        userAddress.setText(currentUser.getAddress());

        //init
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void galleryClicked(View view){

    }
    public void photoClicked(View view){

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menuHandler.onNavigationItemSelected(item);
        return false;
    }
}
