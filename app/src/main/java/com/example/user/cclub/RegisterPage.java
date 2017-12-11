package com.example.user.cclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by user on 12/11/2017.
 */

public class RegisterPage extends AppCompatActivity {
    ImageView imageToUpload;
    ImageButton imgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageToUpload = (ImageView)findViewById(R.id.imageViewInfo);
        imgBtn = (ImageButton) findViewById(R.id.imageButtonReg);
    }


}
