package com.example.locationtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class ModifyWeight extends Activity implements View.OnClickListener {
    private EditText weightText;
    private Button updateBtn;

    private int id;
    String from;

    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.modify_weight);
        setTitle("Modify Weight");
            //Open data base
        dbManager = new DBManager(this);
        dbManager.open();
        //Set variable to correct UI
        weightText = (EditText) findViewById(R.id.weight);
        updateBtn = (Button) findViewById(R.id.btn_update);
            //Recieve the correct information from where the activity came from
        Intent intent = getIntent();
        String index = intent.getStringExtra("id");
        String weight = intent.getStringExtra("weight");
        from = intent.getStringExtra("unique");

        id = Integer.parseInt(index);
        weightText.setText(weight);
        //If updat button is click go to on click
        updateBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
            //If you were in dayA activity it check the workout and edits the weight to correct value user decided
            if(viewId == R.id.btn_update){
                //Check to see if value entered is a number if it isnt send error dialog
            boolean digitOnly = TextUtils.isDigitsOnly(weightText.getText());
            if(digitOnly) {
                if (from.equals("DayA")) {

                    int peso = Integer.parseInt(weightText.getText().toString());
                    dbManager.updateWeight("Workout", id, peso);
                    this.returnHome();
                } else {
                    //If you were in dayB activity it check the workout and edits the weight to correct value user decided
                    int peso = Integer.parseInt(weightText.getText().toString());
                    dbManager.updateWeight("Dayb", id, peso);
                    this.returnHome();
                }
            }else {
                //User entered text and not digits send message to user
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("DIGITS ONLY");
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
        //Make sure to return to correct previous activity
    public void returnHome(){
        if(from.equals("DayA")) {
            Intent home = new Intent(getApplicationContext(), Exercise.class);
            startActivity(home);
            finish();
        }else{
            Intent homeB = new Intent(getApplicationContext(), Dayb.class);
            startActivity(homeB);
            finish();
        }
    }
}
