package com.example.user.cclub;
//error
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import java.io.IOException;


public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMG = 71;
    ImageView imageViewReg;
    ImageButton cameraBtn,galleryBtn;
    Button gotoUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageViewReg = (ImageView)findViewById(R.id.imageViewReg);
        galleryBtn = (ImageButton) findViewById(R.id.galleryBtnReg);
        cameraBtn = (ImageButton) findViewById(R.id.CameraBtnReg);
        gotoUserInfo = (Button) findViewById(R.id.gotoBtnLog);
        cameraBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        imageViewReg.setOnClickListener(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == RESULT_LOAD_IMG) && (resultCode == RESULT_OK) && (data != null)){
            Uri selectImg = data.getData();
            try{
                Bitmap bmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectImg);
                imageViewReg.setImageBitmap(bmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.galleryBtnReg:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMG);
                break;
            case R.id.CameraBtnReg:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            case R.id.gotoBtnReg:
                intent =  new Intent(RegisterPage.this, UserInfo.class);
                startActivity(intent);
        }
    }
}
