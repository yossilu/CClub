package com.example.user.cclub;
//error

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.io.IOException;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    ImageView imageViewReg;
    ImageButton galleryBtn, cameraBtn2;
    AutoCompleteTextView userEmail, userPassword, userVerifPass, userPhone, userFirst, userLast, userAddress,emails,passwords;
    RelativeLayout activity_sign_up;
    Button userRegisterBtn;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth auth;
    private static final int SIGN_IN=123;
    Snackbar snackbar;



    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutReg);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        userPhone = (AutoCompleteTextView) findViewById(R.id.userPhone);
        userFirst = (AutoCompleteTextView) findViewById(R.id.userFirst);
        userLast = (AutoCompleteTextView) findViewById(R.id.userLast);
        userAddress = (AutoCompleteTextView) findViewById(R.id.userAddress);
        emails = (AutoCompleteTextView) findViewById(R.id.userEmail);
        passwords = (AutoCompleteTextView) findViewById(R.id.userPassword);

        activity_sign_up = (RelativeLayout) findViewById(R.id.registerFragment);
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPassword = (AutoCompleteTextView) findViewById(R.id.userPassword);
        userVerifPass = (AutoCompleteTextView) findViewById(R.id.userVerifPass);
        userRegisterBtn = (Button) findViewById(R.id.userRegisterBtn);
        imageViewReg = (ImageView) findViewById(R.id.imageViewReg);
        galleryBtn = (ImageButton) findViewById(R.id.galleryBtnReg);
        cameraBtn2 = findViewById(R.id.photoBtnReg);
        userPhone.setOnClickListener(this);
        userFirst.setOnClickListener(this);
        userLast.setOnClickListener(this);
        userAddress.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        imageViewReg.setOnClickListener(this);
        cameraBtn2.setOnClickListener(this);
        userRegisterBtn.setOnClickListener(this);
        activity_sign_up.setOnClickListener(this);
        emails.setOnClickListener(this);
        passwords.setOnClickListener(this);

        //init firebase
        auth = FirebaseAuth.getInstance();


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
        } else if(requestCode == CAM_REQUEST) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            cameraBtn2.setImageBitmap(thumbnail);
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
                signUpUser(userEmail.getText().toString(),userPassword.getText().toString());
                break;

        }
    }

    private void signUpUser(String email, final String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            snackbar = Snackbar.make(activity_sign_up,"Error: "+task.getException(),snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            if(userVerifPass.getText().toString().equals(password)) {
                                String user = mFirebaseDatabaseReference.push().getKey();

                                Intent intent = new Intent(RegisterPage.this, MapsActivity.class);
                                intent.putExtra("firstname", userFirst.getText().toString());
                                intent.putExtra("lastname", userLast.getText().toString());
                                intent.putExtra("address", userAddress.getText().toString());
                                intent.putExtra("email", emails.getText().toString());
                                intent.putExtra("phonenumber", userPhone.getText().toString());
                                intent.putExtra("password", passwords.getText().toString());

                                startActivity(intent);



                /*
                WRITES THE NEW USER TO JSON FILE IN THE DATA BASE:
                IT TAKES THE DATA FROM THE VARIABLES ABOVE AND PUT IT TO THE USERS TABLE.
                 */
                                mFirebaseDatabaseReference.child("User").child(user).child("First Name").setValue(userFirst.getText().toString());
                                mFirebaseDatabaseReference.child("User").child(user).child("Last Name").setValue(userLast.getText().toString());
                                mFirebaseDatabaseReference.child("User").child(user).child("Address").setValue(userAddress.getText().toString());
                                mFirebaseDatabaseReference.child("User").child(user).child("Email").setValue(emails.getText().toString());
                                mFirebaseDatabaseReference.child("User").child(user).child("Phone Number").setValue(userPhone.getText().toString());

                                //ADD THE USER NAME AND PASSWORD OF THE NEW STUDENT TO THE USERS MANAGEMENT TABLE
                                mFirebaseDatabaseReference.child("USERS MANAGEMENT").child(user).child("Username").setValue(emails.getText().toString());
                                mFirebaseDatabaseReference.child("USERS MANAGEMENT").child(user).child("Password").setValue(passwords.getText().toString());

                                snackbar = Snackbar.make(activity_sign_up, "Register Success: ", snackbar.LENGTH_SHORT);
                                snackbar.show();
                                finish();
                            } else {
                                snackbar = Snackbar.make(activity_sign_up,"password are not the same: "+task.getException(),snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    }
                });
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

}

