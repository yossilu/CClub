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

/**
 * Created by yosil on 12/30/2017.
 */

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText input_email;
    private Button resetPass;
    private TextView backBtn;
    private RelativeLayout activity_forgot;

    private FirebaseAuth auth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_forgot_password);

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
}
