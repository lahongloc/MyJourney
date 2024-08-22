package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ScheduleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert a new schedule
    public long insertSchedule(int categoryId, String sound, String title, String description, String time, String status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_SOUND, sound);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TITLE, title);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TIME, time);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_STATUS, status);
        return database.insert(DatabaseHelper.TABLE_SCHEDULE, null, values);
    }

    // Retrieve all schedules
    public Cursor getAllSchedules() {
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };
        return database.query(DatabaseHelper.TABLE_SCHEDULE, columns, null, null, null, null, null);
    }

    // Retrieve schedules by categoryId
    public Cursor getSchedulesByCategoryId(int categoryId) {
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };
        return database.query(DatabaseHelper.TABLE_SCHEDULE, columns, DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)}, null, null, null);
    }

    public void close() {
        dbHelper.close();
    }
}
