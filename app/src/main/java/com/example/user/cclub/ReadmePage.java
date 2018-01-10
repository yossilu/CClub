package com.example.user.cclub;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

public class ReadmePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readme);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutReadme);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

    /*    Button gotoLogin = (Button) findViewById(R.id.gotoBtnReadme);
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadmePage.this, LoginPage.class);
                startActivity(intent);
            }
        });*/
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
                intent = new Intent(ReadmePage.this, LoginPage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.readme_page:
                Toast.makeText(this,"Going to Readme",Toast.LENGTH_SHORT).show();
                intent = new Intent(ReadmePage.this, ReadmePage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.map_page:
                Toast.makeText(this,"Going to our location",Toast.LENGTH_SHORT).show();
                intent = new Intent(ReadmePage.this, MapsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.dashboard_page:
                Toast.makeText(this,"Going to change password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(ReadmePage.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;
            case R.id.reset_page:
                Toast.makeText(this,"Going to reset password area",Toast.LENGTH_SHORT).show();
                intent = new Intent(ReadmePage.this, ForgotPassword.class);
                startActivity(intent);
                finish();
                break;


        }
        return false;
    }
}
