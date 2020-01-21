package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.widget.CompoundButton.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_linear);
//        setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_relative);

//       final CheckBox  check = (CheckBox)findViewById(R.id.checkbox);
//
//        check.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//            Toast.makeText(MainActivity.this,(MainActivity.this).getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
//            }
//        });
        final Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this,(MainActivity.this).getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });


        final CheckBox  check = (CheckBox)findViewById(R.id.checkbox);

        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChecked = check.isChecked();
                String s = "";
                if(isChecked==true){
                    s=(MainActivity.this).getResources().getString(R.string.on);
                }else{s=(MainActivity.this).getResources().getString(R.string.off);}
                Snackbar snackbar = Snackbar.make(check, (MainActivity.this).getResources().getString(R.string.checkbox1)+" "+s, Snackbar.LENGTH_LONG)
                        .setAction((MainActivity.this).getResources().getString(R.string.undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                check.setChecked(!check.isChecked());
                                Snackbar snackbar1 = Snackbar.make(check, (MainActivity.this).getResources().getString(R.string.restore), Snackbar.LENGTH_LONG);
                                snackbar1.show();
                                //remember to change the status of checked true to false!!!!!!! can get back to the original status
                            }
                        });
                snackbar.show();
            }
        });


        final Switch switcha = (Switch)findViewById(R.id.simpleSwitch);

        switcha.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                String s = "";
                if(isChecked){
                    s=(MainActivity.this).getResources().getString(R.string.on);
                }else{s=(MainActivity.this).getResources().getString(R.string.off);}
                Snackbar snackbar = Snackbar.make(switcha, (MainActivity.this).getResources().getString(R.string.switch1)+" "+s, Snackbar.LENGTH_LONG)
                        .setAction((MainActivity.this).getResources().getString(R.string.undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switcha.setChecked(!isChecked);
                                Snackbar snackbar1 = Snackbar.make(check, (MainActivity.this).getResources().getString(R.string.restore), Snackbar.LENGTH_LONG);
                                snackbar1.show();
                                //remember to change the status of checked true to false!!!!!!! can get back to the original status
                            }
                        });
                snackbar.show();
            }
        });

    }
}
