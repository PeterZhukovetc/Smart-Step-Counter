package com.project.smart_step_counter;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.project.smart_step_counter.data.DatabaseContract;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class ApplicationService extends Service{

    private SensorManager sensorManager;
    //SensorEventListener mListener;
    private Sensor sensor;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private Semaphore semaphore;

    private DailyRunner dailyRunner;

    Integer generalSteps;
    long generalDistance;

    private GoalsCheckThread goalsCheckThread;

    final String LOG_TAG = "myLogs";

    public ApplicationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "ApplicationService created");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(mListener, sensor, 300);

        databaseHelper=new DatabaseHelper(getApplicationContext());

        goalsCheckThread=new GoalsCheckThread();
        goalsCheckThread.start();



        semaphore=new Semaphore(1,true);

        Calendar date=Calendar.getInstance();
//        date.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
        date.set(Calendar.HOUR,0);
        date.set(Calendar.MINUTE,0);
        date.set(Calendar.SECOND,0);

        dailyRunner=new DailyRunner(date, new Runnable() {
            @Override
            public void run() {
                everydayActions();
            }
        },"everyDayThread");
        dailyRunner.start();

//          TimerTask timerTask=new MyTimerTask();
//        Timer timer=new Timer();
//        Thread thread=new MyThread();
//
//        timer.schedule(new Task(thread), date.getTime(),1000*60*60*24);

    }

    final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("meLog","SSSSSSSSSSTTTTTTTTTTTTTEEEEEEEEEPPPPPPPP");
            new Thread(new Runnable() {
                public void run() {
                    database = databaseHelper.getReadableDatabase();
                    String incrementSteps = "UPDATE " + DatabaseContract.Profile.TABLE_NAME
                            + " SET " + DatabaseContract.Profile.COLUMN_STEPS
                            + "=" + DatabaseContract.Profile.COLUMN_STEPS + "+1 "
                            + "WHERE " + DatabaseContract.Profile._ID + "=1;";
                    database.execSQL(incrementSteps);
                }
            }).start();
            return;
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    private class Task extends TimerTask{
        Thread myThread;
        Task(Thread thread){
            this.myThread=thread;
        }
        @Override
        public void run() {
            myThread.start();
        }
    }

    private  class GoalsCheckThread extends Thread{

        @Override
        public void run() {
            try{

                String[] projectionProfile = {
                        DatabaseContract.Profile.COLUMN_HEIGHT,
                        DatabaseContract.Profile.COLUMN_STEPS
                };

                String[] projectionGoals= {
                        DatabaseContract.Goals.COLUMN_NAME,
                        DatabaseContract.Goals.COLUMN_STEPS,
                        DatabaseContract.Goals.COLUMN_DISTANCE,
                        DatabaseContract.Goals.COLUMN_DONE,
                        DatabaseContract.Goals.COLUMN_TYPE
                };
                database = databaseHelper.getReadableDatabase();


                Cursor cursor = database.query(
                        DatabaseContract.Profile.TABLE_NAME,
                        projectionProfile,
                        null,
                        null,
                        null,
                        null,
                        null
                );



                int stepsIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_STEPS);
                int heightIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_HEIGHT);

                cursor.moveToFirst();

                Integer daySteps = cursor.getInt(stepsIndex);
                Integer height = cursor.getInt(heightIndex);
                long dayDistance=Math.round((((height)/4+37)*0.01)*daySteps);

                cursor.close();

                Cursor cursor5 = database.query(
                        DatabaseContract.Goals.TABLE_NAME,
                        projectionGoals,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                int goalNameIndex = cursor5.getColumnIndex(DatabaseContract.Goals.COLUMN_NAME);
                int goalsDoneIndex=cursor5.getColumnIndex(DatabaseContract.Goals.COLUMN_DONE);
                int goalsDistanceIndex=cursor5.getColumnIndex(DatabaseContract.Goals.COLUMN_DISTANCE);
                int goalsStepsIndex=cursor5.getColumnIndex(DatabaseContract.Goals.COLUMN_STEPS);
                int goalsTypeIndex=cursor5.getColumnIndex(DatabaseContract.Goals.COLUMN_TYPE);

                cursor5.moveToFirst();
                if(cursor5.getCount()!=0){
                    do{
                        String goalName = cursor5.getString(goalNameIndex);
                        Integer goalsDone = cursor5.getInt(goalsDoneIndex);
                        Integer goalsDistance = cursor5.getInt(goalsDistanceIndex);
                        Integer goalsSteps = cursor5.getInt(goalsStepsIndex);
                        Integer goalsType = cursor5.getInt(goalsTypeIndex);

                        if(goalsDone==0){
                            if(goalsType==0){
                                if(daySteps>goalsSteps){
                                    ContentValues values=new ContentValues();
                                    values.put(DatabaseContract.Goals.COLUMN_DONE,1);
                                    database.update(DatabaseContract.Goals.TABLE_NAME,values,
                                            DatabaseContract.Goals.COLUMN_NAME+ "= ?",new String[]{goalName});
                                }
                            }
                            else if(goalsType==1){
                                if(dayDistance>goalsDistance){
                                    ContentValues values=new ContentValues();
                                    values.put(DatabaseContract.Goals.COLUMN_DONE,1);
                                    database.update(DatabaseContract.Goals.TABLE_NAME,values,
                                            DatabaseContract.Goals.COLUMN_NAME+ "= ?",new String[]{goalName});
                                }
                            }

                        }
                        Thread.sleep(100);
                    }while(cursor5.moveToNext());
                }


                cursor5.close();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private class DailyRunner{
        private final Runnable dailyTask;
        private final int hour;
        private final int minute;
        private final int second;
        private final String runThreadName;

        public DailyRunner(Calendar timeOfDay, Runnable dailyTask, String runThreadName)
        {
            this.dailyTask = dailyTask;
            this.hour = timeOfDay.get(Calendar.HOUR_OF_DAY);
            this.minute = timeOfDay.get(Calendar.MINUTE);
            this.second = timeOfDay.get(Calendar.SECOND);
            this.runThreadName = runThreadName;
        }

        public void start()
        {
            startTimer();
        }

        private void startTimer(){
            new Timer(runThreadName, true).schedule(new TimerTask() {
                @Override
                public void run() {
                    dailyTask.run();
                    startTimer();
                }
            }, getNextRunTime());
        }

        private Date getNextRunTime()
        {
            Calendar startTime = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            startTime.set(Calendar.SECOND, second);
            startTime.set(Calendar.MILLISECOND, 0);

            if(startTime.before(now) || startTime.equals(now))
            {
                startTime.add(Calendar.DATE, 1);
            }

            return startTime.getTime();
        }
    }

    private void  everydayActions(){
        String[] projectionProfile = {
                DatabaseContract.Profile.COLUMN_HEIGHT,
                DatabaseContract.Profile.COLUMN_STEPS
        };

        String[] projectionGeneral = {
                DatabaseContract.General.COLUMN_STEPS,
                DatabaseContract.General.COLUMN_DISTANCE
        };

        String[]  projectionStatistics = {
                DatabaseContract.Statistics.COLUMN_STEPS,
                DatabaseContract.Statistics.COLUMN_MONTH,
                DatabaseContract.Statistics.COLUMN_DAY
        };

        String[]  projectionRecords = {
                DatabaseContract.Records.COLUMN_STEPS,
                DatabaseContract.Records.COLUMN_DISTANCE,
        };

        database=databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DatabaseContract.Profile.TABLE_NAME,
                projectionProfile,
                null,
                null,
                null,
                null,
                null
        );



        int stepsIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_STEPS);
        int heightIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_HEIGHT);

        cursor.moveToFirst();

        Integer daySteps = cursor.getInt(stepsIndex);
        Integer height = cursor.getInt(heightIndex);

        ContentValues removeValues=new ContentValues();
        removeValues.put(DatabaseContract.Profile.COLUMN_STEPS,0);
        database.update(DatabaseContract.Profile.TABLE_NAME,removeValues,
                DatabaseContract.Profile._ID+ "= ?",new String[]{"1"});

        cursor.close();

        database=databaseHelper.getReadableDatabase();

        Cursor cursor3 = database.query(
                DatabaseContract.General.TABLE_NAME,
                projectionGeneral,
                null,
                null,
                null,
                null,
                null
        );

        cursor3.moveToFirst();
        int generalStepsIndex = cursor3.getColumnIndex(DatabaseContract.General.COLUMN_STEPS);
        int generalDistanceIndex = cursor3.getColumnIndex(DatabaseContract.General.COLUMN_DISTANCE);

        generalSteps = cursor3.getInt(generalStepsIndex);
        generalDistance = cursor3.getInt(generalDistanceIndex);

        cursor3.close();

        generalSteps+=daySteps;
        generalDistance=Math.round(generalDistance+(((height)/4+37)*0.01)*daySteps);

        new Thread(new Runnable() {
            public void run() {
                //database = databaseHelper.getReadableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContract.General.COLUMN_STEPS,generalSteps);
                contentValues.put(DatabaseContract.General.COLUMN_DISTANCE,generalDistance);
                database.update(DatabaseContract.General.TABLE_NAME,
                        contentValues,
                        DatabaseContract.General._ID+ "= ?",
                        new String[]{"1"});
            }
        }).start();
        database=databaseHelper.getReadableDatabase();

        Cursor cursor5 = database.query(
                DatabaseContract.Statistics.TABLE_NAME,
                projectionStatistics,
                null,
                null,
                null,
                null,
                null
        );


        if (cursor5.getCount() == 5) {
            String str="DELETE FROM "+DatabaseContract.Statistics.TABLE_NAME
                    + " WHERE "+ DatabaseContract.Statistics._ID+ " =1 ;";
            database.execSQL(str);
        }

        Calendar calendar=Calendar.getInstance();

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);

        ContentValues contentValues1=new ContentValues();
        contentValues1.put(DatabaseContract.Statistics.COLUMN_STEPS, daySteps);
        contentValues1.put(DatabaseContract.Statistics.COLUMN_MONTH, month);
        contentValues1.put(DatabaseContract.Statistics.COLUMN_DAY, day);

        database.insert(DatabaseContract.Statistics.TABLE_NAME,null,contentValues1);

        cursor5.close();

        database=databaseHelper.getReadableDatabase();

        Cursor cursor4 = database.query(
                DatabaseContract.Records.TABLE_NAME,
                projectionRecords,
                null,
                null,
                null,
                null,
                null
        );

        cursor4.moveToFirst();
        int recordsStepsIndex = cursor4.getColumnIndex(DatabaseContract.Records.COLUMN_STEPS);
        int recordsDistanceIndex = cursor4.getColumnIndex(DatabaseContract.Records.COLUMN_DISTANCE);

        int recordsSteps = cursor4.getInt(recordsStepsIndex);
        int recordsDistance = cursor4.getInt(recordsDistanceIndex);

        if(daySteps>recordsSteps){
            ContentValues records=new ContentValues();
            records.put(DatabaseContract.Records.COLUMN_STEPS,daySteps);
            database.update(DatabaseContract.Records.TABLE_NAME,
                    records,DatabaseContract.Records._ID+"= ?", new String[]{"1"});

        }
        if(Math.round(((height/4+37)*0.01)*daySteps)>recordsDistance){
            ContentValues records=new ContentValues();
            records.put(DatabaseContract.Records.COLUMN_DISTANCE,
                    Math.round(((height)/4+37)*0.01)*daySteps);
            database.update(DatabaseContract.Records.TABLE_NAME,
                    records,DatabaseContract.Records._ID+"= ?", new String[]{"1"});

        }
        cursor4.close();
        return;
    }


    private class MyThread extends Thread{
        @Override
        public void run() {
            String[] projectionProfile = {
                    DatabaseContract.Profile.COLUMN_HEIGHT,
                    DatabaseContract.Profile.COLUMN_STEPS
            };

            String[] projectionGeneral = {
                    DatabaseContract.General.COLUMN_STEPS,
                    DatabaseContract.General.COLUMN_DISTANCE
            };

            String[]  projectionStatistics = {
                    DatabaseContract.Statistics.COLUMN_STEPS,
                    DatabaseContract.Statistics.COLUMN_MONTH,
                    DatabaseContract.Statistics.COLUMN_DAY
            };

            String[]  projectionRecords = {
                    DatabaseContract.Records.COLUMN_STEPS,
                    DatabaseContract.Records.COLUMN_DISTANCE,
            };

            database=databaseHelper.getReadableDatabase();

            Cursor cursor = database.query(
                    DatabaseContract.Profile.TABLE_NAME,
                    projectionProfile,
                    null,
                    null,
                    null,
                    null,
                    null
            );



            int stepsIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_STEPS);
            int heightIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_HEIGHT);

            cursor.moveToFirst();

            Integer daySteps = cursor.getInt(stepsIndex);
            Integer height = cursor.getInt(heightIndex);

            ContentValues removeValues=new ContentValues();
            removeValues.put(DatabaseContract.Profile.COLUMN_STEPS,0);
            database.update(DatabaseContract.Profile.TABLE_NAME,removeValues,
                    DatabaseContract.Profile._ID+ "= ?",new String[]{"1"});

            cursor.close();

            database=databaseHelper.getReadableDatabase();

            Cursor cursor3 = database.query(
                    DatabaseContract.General.TABLE_NAME,
                    projectionGeneral,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            cursor3.moveToFirst();
            int generalStepsIndex = cursor3.getColumnIndex(DatabaseContract.General.COLUMN_STEPS);
            int generalDistanceIndex = cursor3.getColumnIndex(DatabaseContract.General.COLUMN_DISTANCE);

            generalSteps = cursor3.getInt(generalStepsIndex);
            generalDistance = cursor3.getInt(generalDistanceIndex);

            cursor3.close();

            generalSteps+=daySteps;
            generalDistance=Math.round(generalDistance+(((height)/4+37)*0.01)*daySteps);

            new Thread(new Runnable() {
                public void run() {
                    //database = databaseHelper.getReadableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContract.General.COLUMN_STEPS,generalSteps);
                    contentValues.put(DatabaseContract.General.COLUMN_DISTANCE,generalDistance);
                    database.update(DatabaseContract.General.TABLE_NAME,
                            contentValues,
                            DatabaseContract.General._ID+ "= ?",
                            new String[]{"1"});
                }
            }).start();
            database=databaseHelper.getReadableDatabase();

            Cursor cursor5 = database.query(
                    DatabaseContract.Statistics.TABLE_NAME,
                    projectionStatistics,
                    null,
                    null,
                    null,
                    null,
                    null
            );


            if (cursor5.getCount() == 5) {
                String str="DELETE FROM "+DatabaseContract.Statistics.TABLE_NAME
                        + " WHERE "+ DatabaseContract.Statistics._ID+ " =1 ;";
                database.execSQL(str);
            }

            Calendar calendar=Calendar.getInstance();

            int day=calendar.get(Calendar.DAY_OF_MONTH);
            int month=calendar.get(Calendar.MONTH);

            ContentValues contentValues1=new ContentValues();
            contentValues1.put(DatabaseContract.Statistics.COLUMN_STEPS, daySteps);
            contentValues1.put(DatabaseContract.Statistics.COLUMN_MONTH, month);
            contentValues1.put(DatabaseContract.Statistics.COLUMN_DAY, day);

            database.insert(DatabaseContract.Statistics.TABLE_NAME,null,contentValues1);

            cursor5.close();

            database=databaseHelper.getReadableDatabase();

            Cursor cursor4 = database.query(
                    DatabaseContract.Records.TABLE_NAME,
                    projectionRecords,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            cursor4.moveToFirst();
            int recordsStepsIndex = cursor4.getColumnIndex(DatabaseContract.Records.COLUMN_STEPS);
            int recordsDistanceIndex = cursor4.getColumnIndex(DatabaseContract.Records.COLUMN_DISTANCE);

            int recordsSteps = cursor4.getInt(recordsStepsIndex);
            int recordsDistance = cursor4.getInt(recordsDistanceIndex);

            if(daySteps>recordsSteps){
                ContentValues records=new ContentValues();
                records.put(DatabaseContract.Records.COLUMN_STEPS,daySteps);
                database.update(DatabaseContract.Records.TABLE_NAME,
                        records,DatabaseContract.Records._ID+"= ?", new String[]{"1"});

            }
            if(Math.round(((height/4+37)*0.01)*daySteps)>recordsDistance){
                ContentValues records=new ContentValues();
                records.put(DatabaseContract.Records.COLUMN_DISTANCE,
                        Math.round(((height/4+37)*0.01)*daySteps));
                database.update(DatabaseContract.Records.TABLE_NAME,
                        records,DatabaseContract.Records._ID+"= ?", new String[]{"1"});

            }
            cursor4.close();

            return ;

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ApplicationService onStartCom");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "ApplicationService onDestroy");
        sensorManager.unregisterListener(mListener);
        goalsCheckThread.interrupt();
        super.onDestroy();
    }

}
