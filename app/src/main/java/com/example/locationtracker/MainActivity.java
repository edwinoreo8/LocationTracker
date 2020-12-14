package com.example.locationtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//This is the page user will see when they click go on a workout
public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    DBManager dbManager;

    //When the activity is called it will set variables to the appropriate UI we load the home fragment as the first page user will see
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        BottomNavigationView navigationView = findViewById(R.id.navView);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        actionBar.setTitle("Workout");
        loadFragment(new homeFrag());
        //Open database for a check later on
        dbManager = new DBManager(this);
        dbManager.open();
    }
    //This is the menu button which has Logo home and the chart logo
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = (item) -> {
        //If it is the home logo load the correct page if its the graph again load correct page
        switch (item.getItemId()){
            case R.id.home:
                actionBar.setTitle("Workout");
                loadFragment(new homeFrag());
                return true;
            case R.id.graph:
                actionBar.setTitle("Graph");
                loadFragment(new graphFrag());
                return true;
        }
        return false;

    };
        //This is the load the UI so user see the page
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.holder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
        //There will be two buttons on the UI DayA and DayB, this is for approiate action to be taken when the value is clicked
    public void onClick2(View view){
        //This is to send to the correct activity
        Intent intent = new Intent(this, Exercise.class);
        Intent dayb = new Intent(this, Dayb.class);
        int id = view.getId();
        int data = 0;
        if(view.getId() == R.id.dayA){
            data = 1;
            intent.putExtra("This", data);
            startActivity(intent);
        }
        if(view.getId() == R.id.dayB){
            data = 2;
            intent.putExtra("this", data);
            startActivity(dayb);
        }
        //If the Load graph button is clicked we first check if there are any values if there isnt return the graph is empty so we dont throw an error
        if(view.getId() == R.id.graphBtn){
            Cursor check = dbManager.fetchWeight("Squat");
            check.moveToFirst();
            if(check.getCount() != 0){
                Intent graph = new Intent(this, graph.class);
                startActivity(graph);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Graph Is Empty");
                String[] exit = {"Exit"};
                builder.setItems(exit, (dialog, which) -> {
                    if(which ==0){

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        }

    }
}