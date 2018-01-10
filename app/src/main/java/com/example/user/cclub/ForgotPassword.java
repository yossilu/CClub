package com.example.user.cclub;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by yosil on 12/30/2017.
 */

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private EditText input_email;
    private Button resetPass;
    private TextView backBtn;
    private RelativeLayout activity_forgot;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private FirebaseAuth auth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_forgot_password);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutForgot);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        //view
        input_email = (EditText) findViewById(R.id.Email);
        resetPass = (Button) findViewById(R.id.resetBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        activity_forgot = (RelativeLayout) findViewById(R.id.activityForgotFragment);

        backBtn.setOnClickListener(this);
        resetPass.setOnClickListener(this);

        //init firebase
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backBtn){
            startActivity(new Intent(this,MapsActivity.class));
        } else if(view.getId() == R.id.resetBtn) {
            resetPassword(input_email.getText().toString());
        }
    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Snackbar snackbar = Snackbar.make(activity_forgot,"A Password has been sent to your Email: "+email,Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    Snackbar snackbar = Snackbar.make(activity_forgot,"Failed to send password",Snackbar.LENGTH_SHORT);
                    snackbar.show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch(id) {
            case R.id.login_page:
                Toast.makeText(this,"Going to Login",Toast.LENGTH_SHORT).show();
                intent = new Intent(ForgotPassword.this, LoginPage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.readme_page:
                Toast.makeText(this,"Going to Readme",Toast.LENGTH_SHORT).show();
                intent = new Intent(ForgotPassword.this, ReadmePage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.map_page:
                Toast.makeText(this,"Going to our location",Toast.LENGTH_SHORT).show();
                intent = new Intent(ForgotPassword.this, MapsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.dashboard_page:
                Toast.makeText(this,"Going to change password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(ForgotPassword.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;
            case R.id.reset_page:
                Toast.makeText(this,"Going to reset password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(ForgotPassword.this, ForgotPassword.class);
                startActivity(intent);
                finish();
                break;


        }
        return false;
    }
}
