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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Model.User;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Menu references
    MenuHandler menuHandler;
    int menuCurrentID;
    private TextView welcome;
    private EditText newPassword;
    private Button btnChangePass, btnLogout;
    private RelativeLayout Activity_dashboard;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private User currentUser;
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_dashboard);
        //getWindow().setBackgroundDrawableResource(R.drawable.background);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutDash);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        auth = FirebaseAuth.getInstance();
        currentUser = User.getCurrentUser();
        findViewById(R.id.LogOutBtn).setVisibility(View.VISIBLE);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);


        menuCurrentID = R.id.dashboard_page;
        menuHandler = new MenuHandler(this, menuCurrentID);

        welcome = (TextView) findViewById(R.id.dashboardText);
        newPassword = (EditText) findViewById(R.id.newPass);
        btnChangePass = (Button) findViewById(R.id.changePass);
        btnLogout = (Button) findViewById(R.id.LogOutBtn);
        Activity_dashboard = (RelativeLayout) findViewById(R.id.dashboardFragment);

        //init firebase


        //session check
        if (auth.getCurrentUser() != null && User.getCurrentUser() != null) {
            currentUser = User.getCurrentUser();
            welcome.setText("Hi " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
        }


    }

    public void changePasswordClicked(View view) {
        changePassword(newPassword.getText().toString());
    }

    public void logoutClicked(View view) {
        logoutUser();
    }

    private void logoutUser() {
        auth.signOut();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(Dashboard.this, LoginPage.class));
        }
        finish();

    }

    private void changePassword(String newPass) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPass).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(Activity_dashboard, "Password Changed", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(Activity_dashboard, "Failed to change password", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getSupportActionBar() != null) {
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
