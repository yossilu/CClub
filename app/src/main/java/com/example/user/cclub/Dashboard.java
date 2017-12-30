package com.example.user.cclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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


public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private TextView welcome;
    private EditText newPass;
    private Button btnChangePass, btnLogout;
    private RelativeLayout Activity_dashboard;

    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_dashboard);

        welcome = (TextView) findViewById(R.id.dashboardText);
        newPass = (EditText) findViewById(R.id.newPass);
        btnChangePass = (Button) findViewById(R.id.changePass);
        btnLogout = (Button) findViewById(R.id.LogOutBtn);
        Activity_dashboard = (RelativeLayout) findViewById(R.id.dashboardFragment);

        btnChangePass.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        //init firebase
        auth = FirebaseAuth.getInstance();

        //session check
        if (auth.getCurrentUser() != null)
            welcome.setText("Welcome, " + auth.getCurrentUser().getEmail());


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changePass){
            changePassword(newPass.getText().toString());
        } else if(view.getId() == R.id.LogOutBtn){
            logoutUser();
        }
    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(Dashboard.this,MapsActivity.class));
            finish();
        }
    }

    private void changePassword(String newPass) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPass).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(Activity_dashboard,"Password Changed",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
}
