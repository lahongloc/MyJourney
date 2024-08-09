package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;

public class CategoryDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert a new category
    public long insertCategory(int notificationId, String name, String description, String remindTime) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NOTIFICATION_ID, notificationId);
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, name);
        values.put(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME, remindTime);
        return database.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
    }

    // Retrieve all categories
    public Cursor getAllCategories() {
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME,
                DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
                DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME
        };
        return database.query(DatabaseHelper.TABLE_CATEGORY, columns, null, null, null, null, null);
    }

    // Retrieve categories by notificationId
    public Cursor getCategoriesByNotificationId(int notificationId) {
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME,
                DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
                DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME
        };
        return database.query(DatabaseHelper.TABLE_CATEGORY, columns, DatabaseHelper.COLUMN_CATEGORY_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notificationId)}, null, null, null);
    }

    public Cursor getAllCategoriesWithNotifications() {
        String query = "SELECT " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_ID + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_NAME + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME + ", " +
                "n." + DatabaseHelper.COLUMN_NOTIFICATION_CONTENT +
                " FROM " + DatabaseHelper.TABLE_CATEGORY + " c" +
                " LEFT JOIN " + DatabaseHelper.TABLE_NOTIFICATION + " n" +
                " ON c." + DatabaseHelper.COLUMN_CATEGORY_NOTIFICATION_ID + " = n." + DatabaseHelper.COLUMN_NOTIFICATION_ID;

        return database.rawQuery(query, null);
    }

    public void close() {
        dbHelper.close();
    }
}
