package com.project.smart_step_counter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.smart_step_counter.data.DatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "database1";
    final String LOG_TAG = "myLogs";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "DatabaseHelper created");

        String createTable="create table "+ DatabaseContract.Profile.TABLE_NAME
                +" ("+DatabaseContract.Profile._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.Profile.COLUMN_HEIGHT+" INTEGER NOT NULL, "
                + DatabaseContract.Profile.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.Profile.COLUMN_ISFIRST+" INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(createTable);

        createTable="create table "+ DatabaseContract.Goals.TABLE_NAME
                +" ("+DatabaseContract.Goals._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.Goals.COLUMN_TYPE+" INTEGER NOT NULL, "
                + DatabaseContract.Goals.COLUMN_NAME+" TEXT NOT NULL, "
                + DatabaseContract.Goals.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.Goals.COLUMN_DISTANCE+" INTEGER NOT NULL, "
                + DatabaseContract.Goals.COLUMN_DONE+" INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(createTable);

        createTable="create table "+ DatabaseContract.Achievements.TABLE_NAME
                +" ("+DatabaseContract.Achievements._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.Achievements.COLUMN_TYPE+" INTEGER NOT NULL, "
                + DatabaseContract.Achievements.COLUMN_NAME+" TEXT NOT NULL, "
                + DatabaseContract.Achievements.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.Achievements.COLUMN_DISTANCE+" INTEGER NOT NULL, "
                + DatabaseContract.Goals.COLUMN_DONE+" INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(createTable);

        createTable="create table "+ DatabaseContract.Records.TABLE_NAME
                +" ("+DatabaseContract.Records._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.Records.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.Records.COLUMN_DISTANCE+" INTEGER NOT NULL );";
        db.execSQL(createTable);

        createTable="create table "+ DatabaseContract.General.TABLE_NAME
                +" ("+DatabaseContract.General._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.General.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.General.COLUMN_DISTANCE+" INTEGER NOT NULL );";
        db.execSQL(createTable);

        createTable="create table "+ DatabaseContract.Statistics.TABLE_NAME
                +" ("+DatabaseContract.Statistics._ID + " INTEGER primary key autoincrement, "
                + DatabaseContract.Statistics.COLUMN_STEPS+" INTEGER NOT NULL, "
                + DatabaseContract.Statistics.COLUMN_MONTH+" INTEGER NOT NULL, "
                + DatabaseContract.Statistics.COLUMN_DAY+" INTEGER NOT NULL );";
        db.execSQL(createTable);


        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Profile.COLUMN_STEPS,0);
        values.put(DatabaseContract.Profile.COLUMN_HEIGHT,0);
        values.put(DatabaseContract.Profile.COLUMN_ISFIRST,0);
        db.insert(DatabaseContract.Profile.TABLE_NAME,null,values);

//        values=new ContentValues();
//        values.put(DatabaseContract.Goals.COLUMN_STEPS,70);
//        values.put(DatabaseContract.Goals.COLUMN_DISTANCE,0);
//        values.put(DatabaseContract.Goals.COLUMN_NAME,"Моя разминка");
//        values.put(DatabaseContract.Goals.COLUMN_TYPE,0);
//        values.put(DatabaseContract.Goals.COLUMN_DONE,0);
//        db.insert(DatabaseContract.Goals.TABLE_NAME,null,values);

        values=new ContentValues();
        values.put(DatabaseContract.Achievements.COLUMN_STEPS,800);
        values.put(DatabaseContract.Achievements.COLUMN_DISTANCE,0);
        values.put(DatabaseContract.Achievements.COLUMN_NAME,"Разминка");
        values.put(DatabaseContract.Achievements.COLUMN_TYPE,0);
        values.put(DatabaseContract.Achievements.COLUMN_DONE,0);
        db.insert(DatabaseContract.Achievements.TABLE_NAME,null,values);
        values=new ContentValues();
        values.put(DatabaseContract.Achievements.COLUMN_STEPS,0);
        values.put(DatabaseContract.Achievements.COLUMN_DISTANCE,2000);
        values.put(DatabaseContract.Achievements.COLUMN_NAME,"Кросс");
        values.put(DatabaseContract.Achievements.COLUMN_TYPE,1);
        values.put(DatabaseContract.Achievements.COLUMN_DONE,1);
        db.insert(DatabaseContract.Achievements.TABLE_NAME,null,values);

        values=new ContentValues();
        values.put(DatabaseContract.Records.COLUMN_STEPS,0);
        values.put(DatabaseContract.Records.COLUMN_DISTANCE,0);
        db.insert(DatabaseContract.Records.TABLE_NAME,null,values);

        values=new ContentValues();
        values.put(DatabaseContract.General.COLUMN_STEPS,0);
        values.put(DatabaseContract.General.COLUMN_DISTANCE,0);
        db.insert(DatabaseContract.General.TABLE_NAME,null,values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
