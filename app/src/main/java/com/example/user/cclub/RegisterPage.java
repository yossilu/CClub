package com.example.user.cclub;
//error

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    ImageView imageViewReg;
    ImageButton galleryBtn, cameraBtn2;
//    Button gotoUserInfo;
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);
        imageViewReg = (ImageView)findViewById(R.id.imageViewReg);
        galleryBtn = (ImageButton) findViewById(R.id.galleryBtnReg);
        cameraBtn2 = findViewById(R.id.photoBtnReg);
//        gotoUserInfo = (Button) findViewById(R.id.gotoBtnReg);
        galleryBtn.setOnClickListener(this);
        imageViewReg.setOnClickListener(this);
        cameraBtn2.setOnClickListener(this);

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAM_REQUEST);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }



        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       Bitmap bmap;
       if((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (data != null)){
            Uri selectImg = data.getData();
          try{
               bmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectImg);
               imageViewReg.setImageBitmap(bmap);
               Toast.makeText(RegisterPage.this,"Image uploaded successfully",Toast.LENGTH_LONG).show();
            }
          catch (IOException e)
           {
                e.printStackTrace();
           }
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.galleryBtnReg:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMG);
                Toast.makeText(RegisterPage.this,"Please choose a photo from gallery",Toast.LENGTH_LONG).show();
                break;
            case R.id.photoBtnReg:
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoIntent,CAM_REQUEST);
                Toast.makeText(RegisterPage.this,"Please Take a photo",Toast.LENGTH_LONG).show();
                break;
        }
    }

}

