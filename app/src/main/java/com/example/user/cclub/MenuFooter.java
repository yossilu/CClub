package com.example.user.cclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by user on 12/11/2017.
 */

public abstract class MenuFooter extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Totally not finished
                switch(item.getItemId()){

                    case R.id.nav_action_menu: {

                        break;
                    }

                    case R.id.nav_action_dashboard: {

                        break;
                    }

                    case R.id.nav_action_home: {
                        Intent intentReg = new Intent(MenuFooter.this, LoginPage.class);
                        startActivity(intentReg);
                        break;
                    }

                }
                return true;
            }
        });
    }
}
