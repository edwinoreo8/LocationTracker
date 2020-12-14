package com.example.locationtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class DataBaseHelper extends SQLiteOpenHelper {
    //Name of Table
    public static final String Table_Name = "Workout";
    public static final String Day_b = "Dayb";
    public static final String squat = "Squat";
    public static final String dl = "Deadlift";
    public static final String ohp = "OverHeadPress";
    public static final String bench = "Bench";
    public static final String row = "Rows";
    //Table Columns
    public static final String _id = "_id";
    public static final String exercise = "exercise";
    public static final String reps = "fivebyfive";
    public static final String weight = "LB";

    // Database Information
    static final String DB_NAME = "Workouts.DB";

    // database version
    static final int DB_VERSION = 11;

    //Create Table
    // public static final String Create_Table = "create table " + Table_Name + "(" + _Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +  exercise + "TEXT, " + reps + "TEXT," + weight + "TEXT);";
    public static final String Create_Table = "CREATE TABLE " + Table_Name + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  exercise + " TEXT, " + reps + " TEXT, " + weight + " INTEGER );";

    public static final String Create_Table2 = "CREATE TABLE " + Day_b + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  exercise + " TEXT, " + reps + " TEXT, " + weight + " INTEGER );";
    public static final String Create_Squat = "CREATE TABLE " + squat + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  weight + " INTEGER );";
    public static final String Create_DL = "CREATE TABLE " + dl + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  weight + " INTEGER );";
    public static final String Create_OHP = "CREATE TABLE " + ohp + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  weight + " INTEGER );";
    public static final String Create_Bench = "CREATE TABLE " + bench + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  weight + " INTEGER );";
    public static final String Create_Row = "CREATE TABLE " + row + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +  weight + " INTEGER );";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //When first time it was created creat all tables and place them in database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table);
        db.execSQL(Create_Table2);
        db.execSQL(Create_Squat);
        db.execSQL(Create_DL);
        db.execSQL(Create_OHP);
        db.execSQL(Create_Bench);
        db.execSQL(Create_Row);

    }
    //Drop the table if it already exist dont want dups
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        db.execSQL("DROP TABLE IF EXISTS " + Day_b);
        db.execSQL("DROP TABLE IF EXISTS " + squat);
        db.execSQL("DROP TABLE IF EXISTS " + dl);
        db.execSQL("DROP TABLE IF EXISTS " + ohp);
        db.execSQL("DROP TABLE IF EXISTS " + bench);
        db.execSQL("DROP TABLE IF EXISTS " + row);
        onCreate(db);
    }

}
