package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.widget.CompoundButton.*;

public class MainActivity extends AppCompatActivity {

    EditText mEmailView ;
//    EditText  mPasswordView = (EditText)findViewById(R.id.password);


//    String password = mPasswordView.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_linear);
//        setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_lab3_);

         mEmailView = (EditText)findViewById(R.id.email);
//    EditText  mPasswordView = (EditText)findViewById(R.id.password);

        final Button btn = (Button)findViewById(R.id.button);
        loadLogoutInformation();
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startProfileActivity();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveLoginInformation(mEmailView.getText().toString());

    }

    private void startProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("EMAIL",mEmailView.getText().toString());
        startActivity(intent);
//        finish();

    }
    public void saveLoginInformation(String emailx) {
        Context context = MainActivity.this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginEmail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", emailx);
//        editor.putString("Password", password);
        editor.commit();
    }

    public void loadLogoutInformation() {
        SharedPreferences sharedPreferences= getSharedPreferences("LoginEmail", Context .MODE_PRIVATE);
        String emailId = sharedPreferences.getString("Email","");
        mEmailView.setText(emailId);
    }

}
