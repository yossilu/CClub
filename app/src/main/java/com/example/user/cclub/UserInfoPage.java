package com.example.user.cclub;

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
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Model.User;

/**
 * Created by talha on 14/01/2018.
 */


public class UserInfoPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    //Menu references
    User currentUser;
    MenuHandler menuHandler;
    int menuCurrentID;
    AutoCompleteTextView userPhone, userFirst, userLast, userAddress,userEmail;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ImageButton uploadButton;
    private ImageView imageViewReg;
    private Uri selectedImg;
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    private boolean imageFlag = false;
    private boolean isGallery = false;
    private StorageReference storageReference;
    private byte[] dataBytes;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

//        sAnalytics = GoogleAnalytics.getInstance(this);

    //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutInfo);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        //initializing menu
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        menuCurrentID = R.id.info_page;
        menuHandler = new MenuHandler(this,menuCurrentID);


        //init
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        currentUser = User.getCurrentUser();

        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPhone = (AutoCompleteTextView) findViewById(R.id.userPhone);
        userFirst = (AutoCompleteTextView) findViewById(R.id.userFirst);
        userLast = (AutoCompleteTextView) findViewById(R.id.userLast);
        userAddress = (AutoCompleteTextView) findViewById(R.id.userAddress);
        uploadButton = (ImageButton) findViewById(R.id.photoBtnUpload) ;
        uploadButton.setVisibility(View.INVISIBLE);
        imageViewReg = (ImageView) findViewById(R.id.imageViewReg);

        userEmail.setText(currentUser.getEmail());
        userPhone.setText(currentUser.getPhoneNumber());
        userFirst.setText(currentUser.getFirstName());
        userLast.setText(currentUser.getLastName());
        userAddress.setText(currentUser.getAddress());


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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if ((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (intent != null)) {
            selectedImg = intent.getData();
            imageViewReg.setImageURI(selectedImg);
            uploadButton.setVisibility(View.VISIBLE);
            imageFlag = true;
            isGallery = true;
        } else if(requestCode == CAM_REQUEST) {
            Bitmap bit = (Bitmap)intent.getExtras().get("data");
            imageViewReg.setImageBitmap(bit);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataBytes = baos.toByteArray();

            uploadButton.setVisibility(View.VISIBLE);
            imageFlag = true;
            isGallery = false;
        }

    }
    public void galleryClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        Toast.makeText(UserInfoPage.this, "Please choose a photo from gallery", Toast.LENGTH_LONG).show();
    }
    public void photoClicked(View view){
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, CAM_REQUEST);
        Toast.makeText(UserInfoPage.this, "Please Take a photo", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menuHandler.onNavigationItemSelected(item);
        return false;
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
        mDrawerToggle.syncState();



    }

    public void updateClicked(View view){

    }

    public void uploadClicked(View view) {
        UploadTask uploadTask;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference strf = storageReference.child("images").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("privateimg.jpg");
        if (!isGallery) {
            uploadTask = strf.putBytes(dataBytes);
        }
        else{
            uploadTask = strf.putFile(selectedImg);
        }
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserInfoPage.this, "Photo uploaded successfully!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserInfoPage.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
