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
import android.support.design.widget.NavigationView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Model.User;


public class RegisterPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Menu references
    MenuHandler menuHandler;
    int menuCurrentID;
    private static final int CAM_REQUEST = 1313;
    private static final int RESULT_LOAD_IMG = 1;
    ImageView imageViewReg;
    ImageButton galleryBtn, cameraBtn2;
    AutoCompleteTextView userEmail, userPassword, userVerifPass, userPhone, userFirst, userLast, userAddress, emails, passwords;
    RelativeLayout activity_sign_up;
    Button userRegisterBtn;

    private boolean imageFlag = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private DatabaseReference mFirebaseDatabaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private static final int SIGN_IN = 123;
    private Uri selectedImg;
    Snackbar snackbar;
    private FirebaseUser currentFirebaseUser;
    private boolean isGallery = false;
    private byte[] dataBytes;


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);
        //getWindow().setBackgroundDrawableResource(R.drawable.background);


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //action bar init
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutReg);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        menuCurrentID = R.id.readme_page;
        menuHandler = new MenuHandler(this, menuCurrentID);

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

        //init firebase
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if ((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (intent != null)) {
            selectedImg = intent.getData();
            imageViewReg.setImageURI(selectedImg);
            imageFlag = true;
            isGallery = true;
        } else if (requestCode == CAM_REQUEST) {
            Bitmap bit = (Bitmap) intent.getExtras().get("data");
            imageViewReg.setImageBitmap(bit);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataBytes = baos.toByteArray();
            imageFlag = true;
            isGallery = false;
        }


    }


    private boolean validInformation() {    //TODO : add toasts for invalid information
        String name = userFirst.getText().toString().trim();
        String Lname = userLast.getText().toString().trim();
        String email = emails.getText().toString().trim();
        String phone = userPhone.getText().toString().trim();
        String password = passwords.getText().toString();
        String add = userAddress.getText().toString().trim();
        if (name.isEmpty() || Lname.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || add.isEmpty()) {
            return false;
        }
        if (!userVerifPass.getText().toString().equals(password)) {
            return false;
        }
        return true;
    }

    private void signUpUser(String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterPage.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        } else {
                            createUser();
                        }
                    }
                });
    }

    private void createUser() {
        String UserTypeID = "1"; // TODO : change to real USERTYPEID
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String FirstName = userFirst.getText().toString().trim();
        String LastName = userLast.getText().toString().trim();
        String Email = emails.getText().toString().trim();
        String PhoneNumber = userPhone.getText().toString().trim();
        String Password = passwords.getText().toString().trim();
        String Address = userAddress.getText().toString().trim();
        User u = new User(PhoneNumber, FirstName, LastName, Email, Password, Address, UserTypeID);
        User.currentUser = u;
        //insert data in firebase database Users
        mFirebaseDatabaseReference.child(currentFirebaseUser.getUid()).setValue(u);
        Intent intent;
        if (imageFlag && currentFirebaseUser != null) {
            uploadImage();
        }
        intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
        snackbar = Snackbar.make(activity_sign_up, "Register Success: ", snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void uploadImage() {
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
                Toast.makeText(RegisterPage.this, "Photo uploaded successfully!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPage.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

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

    public void registerClicked(View view) {
        if (validInformation())
            signUpUser(userEmail.getText().toString().trim(), userPassword.getText().toString().trim());
        else {
            Toast.makeText(RegisterPage.this, "Not all fields are filled!", Toast.LENGTH_LONG).show();
        }
    }

    public void galleryClicked(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        Toast.makeText(RegisterPage.this, "Please choose a photo from gallery", Toast.LENGTH_LONG).show();
    }

    public void photoClicked(View view) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, CAM_REQUEST);
        Toast.makeText(RegisterPage.this, "Please Take a photo", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menuHandler.onNavigationItemSelected(item);
        return false;
    }

    public void uploadClicked(View view) {

    }
}

