package com.example.locationtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {
    //THIS IS TO INSERT VALUES INTO SQLDATA
    private DBManager dbManager;
    String squat = "Squat";
    String bench ="BenchPress";
    String row = "Row";
    String rep = "5X5";
    String tableName = "Workout";
    int squatWeight= 5;
    int benchWeight=5;
    int rowWeight=5;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private int progr = 0;
    public ProgressBar bar1;
    public ProgressBar bar2;
    public ProgressBar bar3;
    public TextView squatTxt;
    public TextView benchTxt;
    public TextView rowTxt;

    final String[] from = new String[] {DataBaseHelper._id, DataBaseHelper.exercise, DataBaseHelper.reps, DataBaseHelper.weight};

    final int[] to = new int[]{R.id.id, R.id.workout, R.id.rep, R.id.weight };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercsize);
        //Make database
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch(tableName);
        //Put values in database
        populate();


        //Adding the table into our list view
        adapter = new SimpleCursorAdapter(this, R.layout.view_record, cursor, from, to,0);
        adapter.notifyDataSetChanged();
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
            //Our database was click so we send it over to a different activity so user can modify weight
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idText = (TextView) view.findViewById(R.id.id);
                TextView weightText = (TextView) view.findViewById(R.id.weight);

                String index = idText.getText().toString();
                String peso = weightText.getText().toString();
                    //Sending Id, weight and a unique value so modify weight knows which to modify
                Intent modify = new Intent(getApplicationContext(), ModifyWeight.class);
                modify.putExtra("id", index);
                modify.putExtra("weight", peso);
                modify.putExtra("unique", "DayA");
                startActivity(modify);
                finish();


            }
        });
        //This is for the page we have a progress bar set our variables to it and make sure progress is 0
        bar1 = (ProgressBar) findViewById(R.id.progress_circular);
        bar2 = (ProgressBar) findViewById(R.id.progress_circular2);
        bar3 = (ProgressBar) findViewById(R.id.progress_circular3);
        bar1.setProgress(0);
        bar2.setProgress(0);
        bar3.setProgress(0);
        squatTxt = (TextView) findViewById(R.id.squatText1);
        benchTxt = (TextView) findViewById(R.id.benchText);
        rowTxt = (TextView) findViewById(R.id.rowText);
    }

    public void populate(){
        //IS THERE A TABLE? YES JUST UPDATE VALUES. NO INSERT VALUES
        if(dbManager.hasId(tableName,squat)){
            //This is Retriving Table Column Weight
            //Now we are adding the corresponding values to our weight
            Cursor cursorW = dbManager.fetchWeight(tableName);
            cursorW.moveToFirst();
            squatWeight = cursorW.getInt(0);
            cursorW.moveToNext();
            benchWeight = cursorW.getInt(0);
            cursorW.moveToNext();
            rowWeight = cursorW.getInt(0);
            //
            dbManager.update(1,tableName,squat, rep, squatWeight);
            dbManager.update(2,tableName,bench, rep, benchWeight);
            dbManager.update(3,tableName,row, rep, rowWeight);
        }else {

            dbManager.insert(tableName,squat, rep, squatWeight);
            dbManager.insert(tableName,bench, rep, benchWeight);
            dbManager.insert(tableName,row, rep, rowWeight);
        }

    }

    //If the progress bar buttons have been click update the progress bar by 20% as it's only 5 reps to completetion
    public void onBarClick(View view){
        int id = view.getId();
        int temp;
        if(id == R.id.btn_squat1){
            if(bar1.getProgress() != 100){
                temp = bar1.getProgress();
                bar1.setProgress(temp+20);
                //If it's 100% change text to done and update our values in the database
                if(bar1.getProgress() == 100){
                    squatTxt.setText("Done");
                    dbManager.insertWeight("Squat", squatWeight);
                    dbManager.update(1, tableName, squat,rep, squatWeight+5);
                }
            }
        }else if(id == R.id.btn_bench){
            if(bar2.getProgress() != 100){
                temp = bar2.getProgress();
                bar2.setProgress(temp+20);
                //If it's 100% change text to done and update our values in the database
                if(bar2.getProgress() == 100){
                    benchTxt.setText("Done");
                    dbManager.insertWeight("Bench", benchWeight);
                    dbManager.update(2, tableName, bench,rep, benchWeight+5);
                }
            }
        }else if(id == R.id.btn_row){
            if(bar3.getProgress() != 100){
                temp = bar3.getProgress();
                bar3.setProgress(temp+20);
                //If it's 100% change text to done and update our values in the database
                if(bar3.getProgress() == 100){
                    rowTxt.setText("Done");
                    dbManager.insertWeight("Rows", rowWeight);
                    dbManager.update(3,tableName,row, rep, rowWeight+5);
                }
            }
        }else{
            // Done button click let user know next time the weight will increase by 5 lb
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Weight Will Increase By 5lb");
            //builder.setMessage("Weight Will Increase By 5");
            String[] exit = {"Exit"};
            builder.setItems(exit, (dialog, which) -> {
                if(which ==0){
                    dbManager.close();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

    }
}