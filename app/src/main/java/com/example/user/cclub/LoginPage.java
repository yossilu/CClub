package com.example.user.cclub;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.cclub.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginPage extends AppCompatActivity {
    Button gotoreg,LoginBtn,gotoMap,gotoReadme;
    EditText phoneNum,passTxt;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //Init FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        gotoMap = (Button) findViewById(R.id.mapPageBtn);
        gotoreg = (Button) findViewById(R.id.regButton);
        gotoReadme = (Button) findViewById(R.id.readmePageBtn);
        phoneNum = (AutoCompleteTextView) findViewById(R.id.userPhone);
        passTxt = (AutoCompleteTextView) findViewById(R.id.logPassword);

        gotoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intentReg);
            }
        });

        gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(LoginPage.this, MapsActivity.class);
                startActivity(intentMap);
            }
        });

        gotoReadme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReadme = new Intent(LoginPage.this, ReadmePage.class);
                startActivity(intentReadme);
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(LoginPage.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();
                final ValueEventListener valueEventListener = table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user does not exist in the database
                        if (dataSnapshot.child(phoneNum.getText().toString()).exists()) {
                            User user = dataSnapshot.child(phoneNum.getText().toString()).getValue(User.class);
                            System.out.println(passTxt.getText().toString());
                            if ((passTxt.getText().toString()).equals(user.getPassword())) {
                                    Toast.makeText(LoginPage.this,"Signed in successfully",Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(LoginPage.this, MapsActivity.class);
                                    startActivity(homeIntent);
                                    finish();
                            } else {
                                Toast.makeText(LoginPage.this, "could not sign in", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(LoginPage.this, "User not exists, please register or enter correct details", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}

