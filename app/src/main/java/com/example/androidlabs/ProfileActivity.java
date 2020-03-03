package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    ImageButton mImageButton;
    EditText emailEditText;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(ACTIVITY_NAME,"In function:" + "onCreate" );

        setContentView(R.layout.activity_profile);
        emailEditText = (EditText)findViewById(R.id.email1);

        Intent fromMain = getIntent();
        String s = fromMain.getStringExtra("EMAIL");
        emailEditText.setText(s);

        mImageButton = (ImageButton)findViewById(R.id.button2);

        mImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dispatchTakePictureIntent();
            }
        });

        final Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startChatRoomActivity();
            }
        });

        final Button btn4 = (Button)findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startWeatherForecast();
            }
        });
    }

    final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME,"In function:" + "onActivityResult" );
    }


    private void startChatRoomActivity() {
        Intent intent = new Intent(ProfileActivity.this, ChatRoomActivity.class);
        startActivity(intent);
//        finish();
    }

    private void startWeatherForecast() {
        Intent intent = new Intent(ProfileActivity.this, WeatherForecast.class);
        startActivity(intent);
//        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME,"In function:" + "onStart" );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME,"In function:" + "onResume" );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME,"In function:" + "onPause" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME,"In function:" + "onStop" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"In function:" + "onDestroy" );
    }
}
