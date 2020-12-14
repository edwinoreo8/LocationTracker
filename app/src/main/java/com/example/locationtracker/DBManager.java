package com.example.locationtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DataBaseHelper dataBaseHelper;

    private Context context;
    private SQLiteDatabase database;
    public DBManager(Context c){
        context =c;
    }


    //Open the database and get the values in it
    public DBManager open() throws SQLException{

        dataBaseHelper = new DataBaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }
    //Close the data base
    public void close(){
        dataBaseHelper.close();
    }

    public void insert(String table, String workout, String reps, int weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.exercise, workout);
        contentValues.put(DataBaseHelper.reps, reps);
        contentValues.put(DataBaseHelper.weight, weight);
        database.insert(table, null, contentValues);
    }

    //This returns a cursor which is almost like a pointer to the table which you can then iterate through
    public Cursor fetch(String table){
        //  String[] columns = new String[] {DataBaseHelper._id, DataBaseHelper.exercise, DataBaseHelper.reps, DataBaseHelper.weight};
        String[] columns = new String[] {"_id", "exercise", "fivebyfive", "LB"};
        Cursor cursor = database.query(table, columns, null,null,null,null,null);
        if(cursor !=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    //This is to update the the values in the table, DayB and DayA track squat the same so if there is an update to squat it must update both tables
    public int update(long id, String table ,String workout, String reps, int weight){
        int i;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.exercise, workout);
        contentValues.put(DataBaseHelper.reps, reps);
        contentValues.put(DataBaseHelper.weight, weight);
        if(id == 1){
            i=   database.update("Workout", contentValues, DataBaseHelper._id + " = " + id,null );
            database.update("Dayb", contentValues, DataBaseHelper._id + " = " + id,null );
        }else {
            i = database.update(table, contentValues, DataBaseHelper._id + " = " + id, null);
        }
        return i;
    }
    //Users only can update weight this allows them to do so.
    public int updateWeight(String table ,long id, int weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.weight, weight);
        int i = database.update(table, contentValues, DataBaseHelper._id + " = " + id,null );
        return i;
    }

    //This is to check if the table has been made already
    public boolean hasId(String table,String exercise) {

        Cursor cur = database.rawQuery("SELECT * FROM " + table + " WHERE exercise = '" + exercise + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
    //This is to get the weight column from the table choosen
    public Cursor fetchWeight(String table){
        String[]  lb = new String[]{"LB"};
        Cursor cursor = database.query(table, lb, null,null,null,null,null);
        if(cursor !=null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    //This is to insert weight into our Tables for exercise
    public void insertWeight(String table, int weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.weight, weight);
        database.insert(table, null, contentValues);
    }

}
