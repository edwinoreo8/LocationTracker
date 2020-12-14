package com.example.locationtracker;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
//We are using a library that isnt originally in Android studio this is to make graphs from our database
public class graph extends AppCompatActivity {
    TextView text;
    LineChart chart;
    LineChart chart2;
    LineChart chart3;
    LineChart chart4;
    LineChart chart5;

    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //Open Table and then set a point to the begining of each of the respective table
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor squatTbl = dbManager.fetchWeight("Squat");
        squatTbl.moveToFirst();
        Cursor dlTbl = dbManager.fetchWeight("Deadlift");
        dlTbl.moveToFirst();
        Cursor ohpTbl = dbManager.fetchWeight("OverHeadPress");
        ohpTbl.moveToFirst();
        Cursor benchTbl = dbManager.fetchWeight("Bench");
        ohpTbl.moveToFirst();
        Cursor rowTbl = dbManager.fetchWeight("Rows");
        rowTbl.moveToFirst();
        //


        //Setting our chart variables to correct charts
        chart= (LineChart) findViewById(R.id.lineChart);
        chart2 = (LineChart) findViewById(R.id.lineChart2);
        chart3  = (LineChart) findViewById(R.id.lineChart3);
        chart4 = (LineChart) findViewById(R.id.lineChart4);
        chart5 = (LineChart) findViewById(R.id.lineChart5);
        //Squat
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<Entry> squatY = new ArrayList<>();
        //DL
        ArrayList<Entry> dlY = new ArrayList<>();
        //OHP
        ArrayList<Entry> ohpY = new ArrayList<>();
        //Bench
        ArrayList<Entry> benchY = new ArrayList<>();
        //Row
        ArrayList<Entry> rowY = new ArrayList<>();

        int numDataPoint = 50;
        double x =0;
        for(int i =0; i<numDataPoint; i++){
            x = i;
            if(i ==0){
                //First time around look at the very first point of the database of each lift and add them to our Y axis of each lift
                squatY.add(new Entry(squatTbl.getInt(0),i));
                dlY.add(new Entry(dlTbl.getInt(0),i));
                ohpY.add(new Entry(ohpTbl.getInt(0),i));
                benchY.add(new Entry(benchTbl.getInt(0),i));
                rowY.add(new Entry(rowTbl.getInt(0),i));
                xAxis.add(i,String.valueOf(x));
            }else{//If it isnt first time through make sure each workout has a next entry to add into y axis if they dont skip it since there is no value to enter
                if(squatTbl.moveToNext()){
                    Log.d("Test", "THIS IS WEIGHT ENTRY" + squatTbl.getInt(0));
                    squatY.add(new Entry(squatTbl.getInt(0),i));
                }
                if(dlTbl.moveToNext()){
                    dlY.add(new Entry(dlTbl.getInt(0),i));
                    Log.d("Test", "THIS IS DEAD ENTRY" + dlTbl.getInt(0));
                }
                if(ohpTbl.moveToNext()){
                    ohpY.add(new Entry(ohpTbl.getInt(0),i));
                    Log.d("Test", "THIS IS OHP ENTRY" + ohpTbl.getInt(0));
                }
                if (benchTbl.moveToNext()){
                    benchY.add(new Entry(benchTbl.getInt(0),i));
                    Log.d("Test", "THIS IS BENCh ENTRY" + benchTbl.getInt(0));
                }
                if(rowTbl.moveToNext()){
                    rowY.add(new Entry(rowTbl.getInt(0),i));
                    Log.d("Test", "THIS IS ROW ENTRY" + rowTbl.getInt(0));
                }
                //Make sure to add x axis value which is just i
                xAxis.add(i,String.valueOf(x));
            }
        }
        //We are making datasets of all the yAxis so we can draw them into our chart
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets2 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets3 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets4 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets5 = new ArrayList<>();
        //Placing data and giving the chart color
        LineDataSet lineDataSquat = new LineDataSet(squatY, "Squat");
        lineDataSquat.setDrawCircles(false);
        lineDataSquat.setColor(Color.BLUE);
        //Placing data and giving the chart color
        LineDataSet lineDataDl = new LineDataSet(dlY, "DeadLift");
        lineDataDl.setDrawCircles(false);
        lineDataDl.setColor(Color.BLACK);
        //Placing data and giving the chart color
        LineDataSet lineDataOhp = new LineDataSet(ohpY, "OHP");
        lineDataOhp.setDrawCircles(false);
        lineDataOhp.setColor(Color.GREEN);
        //Placing data and giving the chart color
        LineDataSet lineDataBench = new LineDataSet(benchY, "Bench");
        lineDataBench.setDrawCircles(false);
        lineDataBench.setColor(Color.CYAN);
        //Placing data and giving the chart color
        LineDataSet lineDataRows = new LineDataSet(rowY, "Rows");
        lineDataRows.setDrawCircles(false);
        lineDataRows.setColor(Color.RED);
        //Placing adding all the data collected into Ilinedata set as Iline is needed more multiple values
        lineDataSets.add(lineDataSquat);
        lineDataSets2.add(lineDataDl);
        lineDataSets3.add(lineDataOhp);
        lineDataSets4.add(lineDataBench);
        lineDataSets5.add(lineDataRows);
        //Setting the data to place into charts
        LineData data = new LineData(lineDataSets);
        LineData data2 = new LineData(lineDataSets2);
        LineData data3 = new LineData(lineDataSets3);
        LineData data4 = new LineData(lineDataSets4);
        LineData data5 = new LineData(lineDataSets5);

            //Placing value into chart
        chart.setData(data);
        chart2.setData(data2);
        chart3.setData(data3);
        chart4.setData(data4);
        chart5.setData(data5);

        //chart.setVisibleXRangeMaximum(200f);
    }
}