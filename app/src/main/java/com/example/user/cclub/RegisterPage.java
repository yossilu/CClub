package com.example.user.cclub;
//error
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMG = 1;
    CameraPhoto cameraPhoto;
    ImageView imageToUpload , imagePreview;
    ImageButton cameraBtn;
    final int CAMERA_REQUEST = 13323;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageToUpload = (ImageView)findViewById(R.id.imageViewInfo);
        imagePreview = (ImageView)findViewById(R.id.imageViewInfo);
        cameraBtn = (ImageButton) findViewById(R.id.imageButtonReg);
        cameraPhoto = new CameraPhoto(getApplicationContext());

        imageToUpload.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (data != null)){
            Uri selectImg = data.getData();
            imageToUpload.setImageURI(selectImg);
        }
        if(resultCode == RESULT_OK){
            if(resultCode == CAMERA_REQUEST){
                cameraPhoto.getPhotoPath();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent gallery;
        switch (v.getId()) {
            case R.id.imageViewInfo:
                gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,RESULT_LOAD_IMG);
                break;
            case R.id.imageButtonReg:
                startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                cameraPhoto.addToGallery();
                break;
        }
    }
}
