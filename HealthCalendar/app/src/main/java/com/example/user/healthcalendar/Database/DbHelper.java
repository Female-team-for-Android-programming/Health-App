package com.example.user.healthcalendar.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.user.healthcalendar.Database.DatabaseContract.DATABASE_NAME;
import static com.example.user.healthcalendar.Database.DatabaseContract.DATABASE_VERSION;

public class DbHelper  extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDoctors(db);
        createEvents(db);
    }

    private static void createDoctors(SQLiteDatabase db){
        String SQL = "CREATE TABLE IF NOT EXISTS " + DatabaseContract.DoctorsColumns.TABLE_NAME + " ("
                + DatabaseContract.DoctorsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.DoctorsColumns.SPECIALITY + " TEXT NOT NULL, "
                + DatabaseContract.DoctorsColumns.NAME + " TEXT, "
                + DatabaseContract.DoctorsColumns.SURNAME + " TEXT, "
                + DatabaseContract.DoctorsColumns.FATHERSNAME + " TEXT, "
                + DatabaseContract.DoctorsColumns.ADDRESS + " TEXT, "
                + DatabaseContract.DoctorsColumns.CONTACTS + " TEXT, "
                + DatabaseContract.DoctorsColumns.COMMENT + " TEXT);";
        db.execSQL(SQL);
    }

    private static void createEvents(SQLiteDatabase db){

    }

    static void createTable(String tableName, SQLiteDatabase db){
        switch (tableName){
            case DatabaseContract.DoctorsColumns.TABLE_NAME:
                createDoctors(db);
            case DatabaseContract.EventsColumns.TABLE_NAME:
                createEvents(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.DoctorsColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.EventsColumns.TABLE_NAME);

        onCreate(db);

    }
}
