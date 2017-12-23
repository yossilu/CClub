package com.example.user.cclub;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginPage extends AppCompatActivity {
    Button gotoreg,gotoInfo,gotoMap,gotoReadme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        gotoInfo = (Button) findViewById(R.id.infoPageBtn);
        gotoMap = (Button) findViewById(R.id.mapPageBtn);
        gotoreg = (Button) findViewById(R.id.gotoBtnLog);
        gotoReadme = (Button) findViewById(R.id.readmePageBtn);
        gotoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReg = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intentReg);
            }
        });

        gotoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfo;
                intentInfo = new Intent(LoginPage.this, UserInfo.class);
                startActivity(intentInfo);
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

    }
}

