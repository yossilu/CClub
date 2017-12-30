package com.example.user.cclub;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

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

    private FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


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
}




