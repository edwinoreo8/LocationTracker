package com.example.locationtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Dayb extends AppCompatActivity {

    //THIS IS TO INSERT VALUES INTO SQLDATA
    private DBManager dbManager;
    String squat = "Squat";
    String deadlift ="DeadLift";
    String OverHeadPress = "Over Head Press";
    String rep = "5X5";
    String tableName = "Dayb";
    int squatWeight= 5;
    int dlWeight =5;
    int ohpWeight =5;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    public ProgressBar bar4;
    public ProgressBar bar5;
    public ProgressBar bar6;
    public TextView squatTxt;
    public TextView dlTxt;
    public TextView ohpTxt;

    final String[] from = new String[] {DataBaseHelper._id, DataBaseHelper.exercise, DataBaseHelper.reps, DataBaseHelper.weight};

    final int[] to = new int[]{R.id.id, R.id.workout, R.id.rep, R.id.weight };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_b);
        //Make database
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch(tableName);
        //Put values in database
        populate();


        //Adding the table into our list view
        adapter = new SimpleCursorAdapter(this, R.layout.view_record, cursor, from, to,0);
        adapter.notifyDataSetChanged();
        listView = (ListView) findViewById(R.id.list_view2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Our database was click so we send it over to a different activity so user can modify weight
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
                modify.putExtra("unique", "DayB");
                startActivity(modify);
                finish();


            }
        });
        //This is for the page we have a progress bar set our variables to it and make sure progress is 0
        bar4 = (ProgressBar) findViewById(R.id.progress_circular4);
        bar5 = (ProgressBar) findViewById(R.id.progress_circular5);
        bar6 = (ProgressBar) findViewById(R.id.progress_circular6);
        squatTxt = (TextView) findViewById(R.id.squatTxt);
        dlTxt = (TextView) findViewById(R.id.dlTxt);
        ohpTxt = (TextView) findViewById(R.id.ohpTxt);

    }

    public void populate(){
        //IS THERE A TABLE? YES JUST UPDATE VALUES. NO INSERT VALUES
        if(dbManager.hasId(tableName,squat)){
            //This is Retriving Table Column Weight
            //Now we are adding the corresponding values to our weight
            Cursor cursorW = dbManager.fetchWeight(tableName);
            Cursor checkDayA = dbManager.fetchWeight("Workout");
            checkDayA.moveToFirst();
            int checkWeight = checkDayA.getInt(0);
            cursorW.moveToFirst();
            squatWeight = cursorW.getInt(0);
            if(checkWeight > squatWeight){
                squatWeight = checkWeight;
            }
            cursorW.moveToNext();
            dlWeight = cursorW.getInt(0);
            cursorW.moveToNext();
            ohpWeight = cursorW.getInt(0);
            //
            dbManager.update(1,tableName,squat, rep, squatWeight);
            dbManager.update(2,tableName, deadlift, rep, dlWeight);
            dbManager.update(3,tableName, OverHeadPress, rep, ohpWeight);
        }else {
            dbManager.insert(tableName,squat, rep, squatWeight);
            dbManager.insert(tableName, deadlift, rep, dlWeight);
            dbManager.insert(tableName, OverHeadPress, rep, ohpWeight);
        }

    }
    //If the progress bar buttons have been click update the progress bar by 20% as it's only 5 reps to completion
    public void onBarClick2(View view){
        int id = view.getId();
        int temp;
        if(id == R.id.btn_squat2){
            if(bar4.getProgress() != 100){
                temp = bar4.getProgress();
                bar4.setProgress(temp+20);
                if(bar4.getProgress() == 100){
                    squatTxt.setText("Done");
                    dbManager.insertWeight("Squat", squatWeight);
                    dbManager.update(1, tableName, squat,rep, squatWeight+5);
                }
            }
        }else if(id == R.id.btn_dl){
            if(bar5.getProgress() != 100){
                temp = bar5.getProgress();
                bar5.setProgress(temp+20);
                if(bar5.getProgress() == 100){
                    //If it's 100% change text to done and update our values in the database
                    dlTxt.setText("Done");
                    dbManager.insertWeight("DeadLift" , dlWeight);
                    dbManager.update(2,tableName, deadlift, rep, dlWeight +5);
                }
            }
        }else if(id == R.id.btn_ohp){
            if(bar6.getProgress() != 100){
                temp = bar6.getProgress();
                bar6.setProgress(temp+20);
                if(bar6.getProgress() == 100){
                    //If it's 100% change text to done and update our values in the database
                    ohpTxt.setText("Done");
                    dbManager.insertWeight("OverHeadPress" , ohpWeight);
                    dbManager.update(3,tableName, OverHeadPress, rep, ohpWeight +5);
                }
            }
        }else {
            // Done button click let user know next time the weight will increase by 5 lb
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Weight Will Increase By 5lb");
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
