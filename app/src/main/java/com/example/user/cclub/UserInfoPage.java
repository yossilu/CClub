package com.example.user.cclub;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide.*;
import com.bumptech.glide.*;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Model.User;

/**
 * Created by talha on 14/01/2018.
 */

//declaring UserInfoPage implements navigation listener for the main menu
public class UserInfoPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ///////////////DECLARTIONS///////////////////
    //Menu references
    User currentUser;
    MenuHandler menuHandler;
    int menuCurrentID;
    AutoCompleteTextView userPhone, userFirst, userLast, userAddress, userEmail;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ImageButton uploadButton;
    private ImageView imageView;
    private Uri selectedImg;
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    private boolean imageFlag = false;
    private boolean isGallery = false;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Bitmap currentPicture;
    private byte[] dataBytes;

    //OnCreate would populate the declared Activity fields
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        //getWindow().setBackgroundDrawableResource(R.drawable.background);

//        sAnalytics = GoogleAnalytics.getInstance(this);

        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutInfo);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        //initializing menu
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        menuCurrentID = R.id.info_page;
        menuHandler = new MenuHandler(this, menuCurrentID);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //init
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        currentUser = User.getCurrentUser();
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userPhone = (AutoCompleteTextView) findViewById(R.id.userPhone);
        userFirst = (AutoCompleteTextView) findViewById(R.id.userFirst);
        userLast = (AutoCompleteTextView) findViewById(R.id.userLast);
        userAddress = (AutoCompleteTextView) findViewById(R.id.userAddress);
        uploadButton = (ImageButton) findViewById(R.id.photoBtnUpload);
        uploadButton.setVisibility(View.INVISIBLE);
        imageView = (ImageView) findViewById(R.id.image_view);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference strf = storageReference.child("images").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("privateimg.jpg");
        String filePath = "gs://c-club-86d82.appspot.com/" + strf.getPath().toString();
        //pictureThread.start();
        Glide.with(this).using(new FirebaseImageLoader()).load(strf).into(imageView);
        userEmail.setText(currentUser.getEmail());
        userEmail.setActivated(false);
        userPhone.setText(currentUser.getPhoneNumber());
        userFirst.setText(currentUser.getFirstName());
        userLast.setText(currentUser.getLastName());
        userAddress.setText(currentUser.getAddress());


    }
    /*
        handling the answer from the user about Camera permission
     */
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

    /*
             handling the result (file/image/bitmap) from the ACTION_IMAGE_CAPTURE or ACTION_PICK
                 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if ((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (intent != null)) {
            selectedImg = intent.getData();
            imageView.setImageURI(selectedImg);
            uploadButton.setVisibility(View.VISIBLE);
            imageFlag = true;
            isGallery = true;
        } else if (requestCode == CAM_REQUEST) {
            Bitmap bit = (Bitmap) intent.getExtras().get("data");
            imageView.setImageBitmap(bit);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataBytes = baos.toByteArray();

            uploadButton.setVisibility(View.VISIBLE);
            imageFlag = true;
            isGallery = false;
        }

    }
    /*
            Click listener method for GalleryButton (opening the gallery to choose photo to upload)
            ACTION_PICK
         */
    public void galleryClicked(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        Toast.makeText(UserInfoPage.this, "Please choose a photo from gallery", Toast.LENGTH_LONG).show();
    }
    /*
                Click listener method for PhotoButton (opening the gallery to choose photo to upload)
                ACTION_IMAGE_CAPTURE
             */
    public void photoClicked(View view) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, CAM_REQUEST);
        Toast.makeText(UserInfoPage.this, "Please Take a photo", Toast.LENGTH_LONG).show();
    }

    /*
        View handler for the navigation menu (colors and etc.)
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    /*
        View handler for the navigation menu (colors and etc.)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
//
        return super.onOptionsItemSelected(item);
    }

    /*
            View handler for the navigation menu (colors and etc.)
         */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
         Listener method, handling the selected item from the menu
         and navigating to the clicked page (by item)
             */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menuHandler.onNavigationItemSelected(item);
        return false;
    }
    /*
        initializing the navigator
         */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open, R.string.close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getSupportActionBar() != null) {
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
    /*
        checking if the user fields with valid characters and length
         */
    private boolean validInformation() {    //TODO : add toasts for invalid information
        String name = "" + userFirst.getText().toString().trim();
        String Lname = "" + userLast.getText().toString().trim();
        String phone = "" + userPhone.getText().toString().trim();
        String add = "" + userAddress.getText().toString().trim();
        if (name.length() < 3 || Lname.length() < 3 || phone.length() < 3 || add.length() < 3) {
            Toast.makeText(UserInfoPage.this, "All fields must be filled and contain at least 3 letters", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*
            Click listener method for UpdateButton
         */
    public void updateClicked(View view) {
        if (validInformation()) {
            FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference dbrf = databaseReference.child(fu.getUid().toString());
            dbrf.child("phoneNumber").setValue(userPhone.getText().toString().trim());
            dbrf.child("firstName").setValue(userFirst.getText().toString().trim());
            dbrf.child("lastName").setValue(userLast.getText().toString().trim());
            dbrf.child("address").setValue(userAddress.getText().toString().trim());
            if (imageFlag){
                uploadPhoto();
            }
            Toast.makeText(UserInfoPage.this, "Updated Successfully!", Toast.LENGTH_LONG).show();
        }
    }
    /*
            Click listener method for UploadButton
         */
    public void uploadClicked(View view) {
        uploadPhoto();
    }

  /*
  Uploading the photo that received from the user by ACTION_PICK or ACTION_IMAGE_CAPTURE
   */
    public void uploadPhoto(){
        UploadTask uploadTask;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference strf = storageReference.child("images").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("privateimg.jpg");
        if (!isGallery) {
            uploadTask = strf.putBytes(dataBytes);
        } else {
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
