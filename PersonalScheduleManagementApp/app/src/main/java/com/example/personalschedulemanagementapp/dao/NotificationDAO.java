package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;

public class NotificationDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public NotificationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert a new notification
    public long insertNotification(String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_CONTENT, content);
        return database.insert(DatabaseHelper.TABLE_NOTIFICATION, null, values);
    }

    // Retrieve all notifications
    public Cursor getAllNotifications() {
        String[] columns = {
                DatabaseHelper.COLUMN_NOTIFICATION_ID,
                DatabaseHelper.COLUMN_NOTIFICATION_CONTENT
        };
        return database.query(DatabaseHelper.TABLE_NOTIFICATION, columns, null, null, null, null, null);
    }

    // Delete a notification by ID
    public int deleteNotification(long id) {
        return database.delete(DatabaseHelper.TABLE_NOTIFICATION, DatabaseHelper.COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void close() {
        dbHelper.close();
    }
}

