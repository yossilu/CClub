package com.example.user.cclub;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    Button gotoreg,LoginBtn,gotoReadme,mapPageBtn;
    EditText userEmail, userPass;
    RelativeLayout activity_menu;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        setTheme(android.R.style.Theme_Holo);
        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
//
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            // method invoked only when the actionBar is not null.
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        //init
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        gotoreg = (Button) findViewById(R.id.regButton);
        gotoReadme = (Button) findViewById(R.id.readmePageBtn);
        mapPageBtn = (Button) findViewById(R.id.mapPageBtn);
        activity_menu = (RelativeLayout) findViewById(R.id.loginFragment);
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPass = (AutoCompleteTextView) findViewById(R.id.userPass);

        gotoReadme.setOnClickListener(this);
        mapPageBtn.setOnClickListener(this);
        activity_menu.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        gotoreg.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //check session, if ok go to menu
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginPage.this,MapsActivity.class));

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.regButton){
            Intent intentReg = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intentReg);
            finish();
        } else if(view.getId() == R.id.LoginBtn){
            loginUser(userEmail.getText().toString(),userPass.getText().toString());
        } else if(view.getId() == R.id.readmePageBtn) {
            Intent intentReadme = new Intent(LoginPage.this, ReadmePage.class);
            startActivity(intentReadme);
        }
    }

    private void loginUser(String email,final String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    if(pass.length() < 0) {
                        Snackbar snackbar = Snackbar.make(activity_menu,"Password length must be over 6",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } else {
                    startActivity(new Intent(LoginPage.this,LoginPage.class));
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
}




