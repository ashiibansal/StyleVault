package com.example.loginpage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name & Version
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Body Measurements Table
    private static final String TABLE_BODY = "body_measurements";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_SHOULDER = "shoulder";
    private static final String COLUMN_CHEST = "chest";
    private static final String COLUMN_WAIST = "waist";
    private static final String COLUMN_BODY_TYPE = "body_type";

    // Create Users Table Query
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT UNIQUE, " +
            COLUMN_EMAIL + " TEXT UNIQUE, " +
            COLUMN_PASSWORD + " TEXT);";

    // Create Body Measurements Table Query
    private static final String CREATE_BODY_TABLE = "CREATE TABLE " + TABLE_BODY + " (" +
            COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
            COLUMN_HEIGHT + " REAL, " +
            COLUMN_WEIGHT + " REAL, " +
            COLUMN_SHOULDER + " REAL, " +
            COLUMN_CHEST + " REAL, " +
            COLUMN_WAIST + " REAL, " +
            COLUMN_BODY_TYPE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_BODY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY);
        onCreate(db);
    }

    // Method to register a new user
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Method to validate login
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Save or update body measurements
    public void saveBodyMeasurement(String email, double height, double weight, double shoulder,
                                    double chest, double waist, String bodyType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_SHOULDER, shoulder);
        values.put(COLUMN_CHEST, chest);
        values.put(COLUMN_WAIST, waist);
        values.put(COLUMN_BODY_TYPE, bodyType);

        int rows = db.update(TABLE_BODY, values, COLUMN_EMAIL + "=?", new String[]{email});
        if (rows == 0) {
            db.insert(TABLE_BODY, null, values);
        }
        db.close();
    }

    // Get last body measurement for user
    public Cursor getLastBodyMeasurement(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BODY + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
    }
}
