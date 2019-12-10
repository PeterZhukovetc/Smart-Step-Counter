package com.project.smart_step_counter.data;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract(){}
    public final static class Profile implements BaseColumns{
        public final static String TABLE_NAME = "profile";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_HEIGHT="height";
        public final static String COLUMN_ISFIRST="isFirst";
    }
    public final static class Goals implements BaseColumns{
        public final static String TABLE_NAME = "goals";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_DISTANCE="distance";
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_TYPE="type";
        public final static String COLUMN_DONE="done";

    }
    public final static class Achievements implements BaseColumns{
        public final static String TABLE_NAME = "achievements";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_DISTANCE="distance";
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_TYPE="type";
        public final static String COLUMN_DONE="done";
    }
    public final static class Records implements BaseColumns{
        public final static String TABLE_NAME = "records";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_DISTANCE="distance";
    }
    public final static class General implements BaseColumns{
        public final static String TABLE_NAME = "general";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_DISTANCE="distance";
    }
    public final static class Statistics implements BaseColumns{
        public final static String TABLE_NAME = "statistics";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_STEPS="steps";
        public final static String COLUMN_MONTH="month";
        public final static String COLUMN_DAY="day";
    }
}
