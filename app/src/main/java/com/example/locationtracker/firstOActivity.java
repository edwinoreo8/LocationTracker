package com.example.locationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

//Very first page that is opened on the app
public class firstOActivity extends AppCompatActivity {
    ViewPager pager;
    Button btn;
    @Override
    //When the activity is first started set values to correct buttons
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_o);
        pager = findViewById(R.id.vpMain);
        pager.setAdapter(new firstAdapter(getSupportFragmentManager()));
        btn = (Button) findViewById(R.id.btn1);
    }

    //If one of the buttons was clicked in the first part of the page either send it to the Running/GPS tracking app
    //OR go to the lifting program.
    public void firstOnclick(View view){

        Intent intent = new Intent(this, MapsActivity.class);
        Intent workout = new Intent(this, MainActivity.class);
                int id = view.getId();
                if(id == R.id.btn1 ){
                    startActivity(intent);
                }else{
                    startActivity(workout);

                }



    }
}