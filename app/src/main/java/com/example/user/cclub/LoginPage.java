package com.example.user.cclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.User;


public class LoginPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    User currentUser;
    Button gotoreg,LoginBtn;
    EditText userEmail, userPass;
    Bundle params;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private FirebaseAuth auth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String buttonClicked;
    FirebaseUser user;
    ArrayList<User> users=new ArrayList<User>();;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        sAnalytics = GoogleAnalytics.getInstance(this);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutLogin);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        //init
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        gotoreg = (Button) findViewById(R.id.regButton);
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPass = (AutoCompleteTextView) findViewById(R.id.userPass);


        //google analytic
        sTracker = sAnalytics.newTracker("UA-112233096-1");
        sTracker.enableAdvertisingIdCollection(true);
        sTracker.enableExceptionReporting(true);
        sTracker.enableAutoActivityTracking(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        params  = new Bundle();

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //check session, if ok go to menu
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginPage.this,MapsActivity.class));

    }




    private void loginUser(final String email,final String pass) {

            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w(mActivityTitle, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginPage.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(mActivityTitle, "signInWithEmail:success");
                        user = auth.getCurrentUser();
                        DataSnapshot dst;
                        DatabaseReference dbRef;
                        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        DatabaseReference dbRef2 = dbRef.child(user.getUid());
                        dbRef.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                currentUser = dataSnapshot.getValue(User.class);
                                String phoneNumber,firstName,lastName,email,password,address,userTypeID;
                                Log.w("TAG", "hi");

                               // for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    currentUser = new User(dataSnapshot.child("phoneNumber").getValue().toString(),dataSnapshot.child("firstName").getValue().toString(),
                                            dataSnapshot.child("lastName").getValue().toString(),dataSnapshot.child("email").getValue().toString(),dataSnapshot.child("password").getValue().toString(),
                                            dataSnapshot.child("address").getValue().toString(), dataSnapshot.child("userTypeID").getValue().toString());
//                                    currentUser.setAddress( postSnapshot.child("address").getValue().toString());
//                                    currentUser.setEmail(postSnapshot.child("email").getValue().toString());
//                                    currentUser.setPassword( postSnapshot.child("password").getValue().toString());
//                                    currentUser.setFirstName( postSnapshot.child("firstName").getValue().toString());
//                                    currentUser.setLastName( postSnapshot.child("lastName").getValue().toString());
//                                    currentUser.setPhoneNumber(postSnapshot.child("phoneNumber").getValue().toString());
//                                    currentUser.setUserTypeID( postSnapshot.child("userTypeID").getValue().toString());
                                    users.add(currentUser);
                            //    }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        dbRef2.child("userTypeID").setValue("1");
                        findViewById(R.id.LoginBtn).setVisibility(View.GONE);
                        startActivity(new Intent(LoginPage.this, MapsActivity.class));

                    }
                }
            });

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
        mDrawerToggle.syncState();



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
        Intent intent;
        int id = item.getItemId();
        switch(id) {
            case R.id.login_page:
                Toast.makeText(this,"Already at Login page",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.readme_page:
                Toast.makeText(this,"Going to Readme",Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginPage.this, ReadmePage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.map_page:
                Toast.makeText(this,"Going to our location",Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginPage.this, MapsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.dashboard_page:
                Toast.makeText(this,"Going to change password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;
            case R.id.reset_page:
                Toast.makeText(this,"Going to reset password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
                finish();
                break;


        }
        return false;
    }

    public void loginClicked(View view){
        if (isValidInformation())
             loginUser(userEmail.getText().toString(), userPass.getText().toString());
        params.putInt("ButtonID",view.getId());
        buttonClicked = "Login_Button";
        mFirebaseAnalytics.logEvent(buttonClicked,params);
    }

    private boolean isValidInformation() {
        String email = userEmail.getText().toString(), pass = userPass.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"All fields must be filled",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (pass.length() < 6){
            Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void registerClicked(View view) {
        params.putInt("ButtonID",view.getId());
        buttonClicked = "Register_Button";
        mFirebaseAnalytics.logEvent(buttonClicked,params);
        Intent intentReg = new Intent(LoginPage.this, RegisterPage.class);
        startActivity(intentReg);
    }

}




