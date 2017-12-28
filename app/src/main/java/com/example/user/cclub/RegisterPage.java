package com.example.user.cclub;
//error

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.cclub.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    ImageView imageViewReg;
    ImageButton galleryBtn, cameraBtn2;
    AutoCompleteTextView userEmail, userPassword, userFirst, userLast, userAdd, userPhone;
    Button userRegisterBtn;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);

        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPassword = (AutoCompleteTextView) findViewById(R.id.userPassword);
        userFirst = (AutoCompleteTextView) findViewById(R.id.userFirst);
        userLast = (AutoCompleteTextView) findViewById(R.id.userLast);
        userAdd = (AutoCompleteTextView) findViewById(R.id.userAddress);
        userPhone = (AutoCompleteTextView) findViewById(R.id.userPhone);
        userRegisterBtn = (Button) findViewById(R.id.userRegisterBtn);
        imageViewReg = (ImageView) findViewById(R.id.imageViewReg);
        galleryBtn = (ImageButton) findViewById(R.id.galleryBtnReg);
        cameraBtn2 = findViewById(R.id.photoBtnReg);
        galleryBtn.setOnClickListener(this);
        imageViewReg.setOnClickListener(this);
        cameraBtn2.setOnClickListener(this);
        userRegisterBtn.setOnClickListener(this);


        //Init FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        userRegisterBtn.setOnClickListener(new View.OnClickListener() {

                                               @Override
                                               public void onClick(View v) {
                                                   final ProgressDialog mDialog = new ProgressDialog(RegisterPage.this);
                                                   mDialog.setMessage("Please wait...");
                                                   mDialog.show();
                                                   table_user.addValueEventListener(new ValueEventListener(){


                                                       @Override
                                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                                            //check if already in the system
                                                           if(dataSnapshot.child(userEmail.getText().toString()).exists()){
                                                               mDialog.dismiss();
                                                               Toast.makeText(RegisterPage.this, "Email is already taken", Toast.LENGTH_SHORT).show();
                                                           }
                                                           else if(dataSnapshot.child(userPhone.getText().toString()).exists()){
                                                               mDialog.dismiss();
                                                               Toast.makeText(RegisterPage.this, "Phone is already taken", Toast.LENGTH_SHORT).show();
                                                           }
                                                           else
                                                           {
                                                               mDialog.dismiss();
                                                               User user = new User(userPhone.getText().toString(),userEmail.getText().toString(),userPassword.getText().toString(),userFirst.getText().toString(),userAdd.getText().toString());
                                                               table_user.child(userPhone.getText().toString()).setValue(user);
                                                               Toast.makeText(RegisterPage.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                           }
                                                       }

                                                       @Override
                                                       public void onCancelled(DatabaseError databaseError) {

                                                       }
                                                   });
                                               }
                                           }

        );


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
        boolean flag = false;
        Bitmap bmap;
        if ((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (data != null)) {
            Uri selectImg = data.getData();
            try {
                bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImg);
                imageViewReg.setImageBitmap(bmap);
                Toast.makeText(RegisterPage.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ((requestCode == CAM_REQUEST) && (resultCode == RESULT_OK) && (data != null)) {
            Uri selectImg = data.getData();
            try {
                bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImg);
                imageViewReg.setImageBitmap(bmap);
                Toast.makeText(RegisterPage.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.galleryBtnReg:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                Toast.makeText(RegisterPage.this, "Please choose a photo from gallery", Toast.LENGTH_LONG).show();
                break;
            case R.id.photoBtnReg:
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoIntent, CAM_REQUEST);
                Toast.makeText(RegisterPage.this, "Please Take a photo", Toast.LENGTH_LONG).show();
                break;
            case R.id.userRegisterBtn:


        }
    }

}

