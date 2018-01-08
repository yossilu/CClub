package com.example.user.cclub;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;


import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Button gotoreg,LoginBtn;
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
        activity_menu = (RelativeLayout) findViewById(R.id.loginFragment);
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPass = (AutoCompleteTextView) findViewById(R.id.userPass);

        activity_menu.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        gotoreg.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //check session, if ok go to menu
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginPage.this,MapsActivity.class));

    }

    public void onClick(View view) {
        if(view.getId() == R.id.regButton){
            Intent intentReg = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intentReg);
            finish();
        } else if((view.getId() == R.id.LoginBtn) && null != LoginBtn.getText().toString()){
            if(userEmail.getText().toString() != null || userPass.getText().toString() != null ) {
                loginUser(userEmail.getText().toString(), userPass.getText().toString());
            } else {
                Toast.makeText(this,"One of the fields has no details",Toast.LENGTH_SHORT).show();
            }
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
                Toast.makeText(this,"Going to Login",Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
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

}




