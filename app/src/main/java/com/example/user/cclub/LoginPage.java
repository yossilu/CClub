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
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.User;


public class LoginPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Menu references
    MenuHandler menuHandler;
    int menuCurrentID;

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

        //initializing menu
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        menuCurrentID = R.id.login_page;
        menuHandler = new MenuHandler(this,menuCurrentID);


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
        currentUser = User.getCurrentUser();
        //check session, if ok go to menu
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginPage.this, Dashboard.class));
            finish();
        }
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
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String uid = auth.getCurrentUser().getUid();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String value = String.valueOf(child.getKey());
                                    if (child.getKey().toString().equals(uid))
                                       currentUser = child.getValue(User.class);
                                }
                                String phoneNumber,firstName,lastName,email,password,address,userTypeID;
                                Log.w("TAG", "hi");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
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
        menuHandler.onNavigationItemSelected(item);
        return false;
    }

    public void loginClicked(View view){
        if (isValidInformation())
             loginUser(userEmail.getText().toString(), userPass.getText().toString());
        params.putInt("ButtonID",view.getId());
        buttonClicked = "Login_Button";
        mFirebaseAnalytics.logEvent(buttonClicked,params);
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginPage.this, Dashboard.class));
            finish();
        }
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




