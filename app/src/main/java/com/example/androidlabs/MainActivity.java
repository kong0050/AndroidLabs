package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_linear);
//        setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_relative);

       final CheckBox  check = (CheckBox)findViewById(R.id.checkbox);

        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            Toast.makeText(MainActivity.this,(MainActivity.this).getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });



        String s = "";
        Snackbar snackbar = Snackbar.make(check, "Switch is"+s, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(check, "Message is restored!", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }
}
