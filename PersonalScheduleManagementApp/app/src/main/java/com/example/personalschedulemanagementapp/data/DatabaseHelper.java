package com.example.personalschedulemanagementapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journey";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USER = "User";
    public static final String TABLE_SOUND = "Sound";
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_SCHEDULE = "Schedule";

    // User Table Columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_FULLNAME = "fullname";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_ROLE = "role";

    // Sound Table Columns
    public static final String COLUMN_SOUND_ID = "id";
    public static final String COLUMN_SOUND_NAME = "name";
//    public static final String COLUMN_SOUND_SOUNDID = "soundId";
    public static final String COLUMN_SOUND_URI = "uri";

    // Category Table Columns
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_SOUND_ID = "soundId";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY_REMIND_TIME = "remindTime";

    // Schedule Table Columns
    public static final String COLUMN_SCHEDULE_ID = "id";
    public static final String COLUMN_SCHEDULE_CATEGORY_ID = "categoryId";
    public static final String COLUMN_SCHEDULE_USER_ID = "userId";
    public static final String COLUMN_SCHEDULE_TITLE = "title";
    public static final String COLUMN_SCHEDULE_DESCRIPTION = "description";
    public static final String COLUMN_SCHEDULE_TIME = "time";
    public static final String COLUMN_SCHEDULE_STATUS = "status";

    // Create User Table SQL
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_USER_FULLNAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_USER_ROLE + " TEXT);";

    // Create Sound Table SQL
    private static final String CREATE_SOUND_TABLE =
            "CREATE TABLE " + TABLE_SOUND + " (" +
                    COLUMN_SOUND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SOUND_NAME + " TEXT NOT NULL, " +
                    COLUMN_SOUND_URI + " TEXT NOT NULL);";
//                    COLUMN_SOUND_SOUNDID + " INTERGER NOT NULL);";

    // Create Category Table SQL
    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + TABLE_CATEGORY + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_SOUND_ID + " INTEGER, " +
                    COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY_DESCRIPTION + " TEXT, " +
                    COLUMN_CATEGORY_REMIND_TIME + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CATEGORY_SOUND_ID + ") REFERENCES " + TABLE_SOUND + "(" + COLUMN_SOUND_ID + "));";

    // Create Schedule Table SQL
    private static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                    COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SCHEDULE_CATEGORY_ID + " INTEGER, " +
                    COLUMN_SCHEDULE_USER_ID + " INTEGER, " +
                    COLUMN_SCHEDULE_TITLE + " TEXT NOT NULL, " +
                    COLUMN_SCHEDULE_DESCRIPTION + " TEXT, " +
                    COLUMN_SCHEDULE_TIME + " INTEGER, " +
                    COLUMN_SCHEDULE_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_SCHEDULE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_SCHEDULE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SOUND_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}

