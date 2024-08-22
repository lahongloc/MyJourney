package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.Sound;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert a new category
    public void insertOrUpdateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_SOUND_ID, category.getSound().getSoundId());
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.getName());
        values.put(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        values.put(DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME, category.getRemindTime());

        if (category.getId() > 0) {
            String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
            String[] selectionArgs = { String.valueOf(category.getId()) };

            database.update(DatabaseHelper.TABLE_CATEGORY, values, selection, selectionArgs);
        } else {
            database.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
        }
    }

    // Delete a category
    public int deleteCategory(int id) {
        String whereClause = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        return database.delete(DatabaseHelper.TABLE_CATEGORY, whereClause, whereArgs);
    }

    // Retrieve all categories as List<Category>
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_SOUND_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME,
                DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
                DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME
        };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORY, columns, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Category category = cursorToCategory(cursor);
                categories.add(category);
            }
        }

        return categories;
    }

    // Retrieve a specific category by ID
    public Category getCategoryById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_SOUND_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME,
                DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
                DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME
        };

        String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursorToCategory(cursor);
            }
        }

        return null;
    }

    // Convert Cursor to Category
    private Category cursorToCategory(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));
        int soundId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_SOUND_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION));
        int remindTime = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME));

        Sound sound = new Sound("sound", soundId); // Assuming Sound has a constructor that takes id and name

        return new Category(id, name, description, remindTime, sound);
    }

    // Retrieve categories by notificationId as List<Category>
    public List<Category> getCategoriesByNotificationId(int notificationId) {
        List<Category> categories = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_CATEGORY_ID,
                DatabaseHelper.COLUMN_CATEGORY_NAME,
                DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION,
                DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME
        };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORY, columns, DatabaseHelper.COLUMN_CATEGORY_SOUND_ID + " = ?", new String[]{String.valueOf(notificationId)}, null, null, null)) {
            while (cursor.moveToNext()) {
                Category category = cursorToCategory(cursor);
                categories.add(category);
            }
        }

        return categories;
    }

    // Retrieve all categories with notifications as List<Category>
    public List<Category> getAllCategoriesWithNotifications() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_ID + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_NAME + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME + ", " +
                "n." + DatabaseHelper.COLUMN_SOUND_SOUNDID +
                " FROM " + DatabaseHelper.TABLE_CATEGORY + " c" +
                " LEFT JOIN " + DatabaseHelper.TABLE_SOUND + " n" +
                " ON c." + DatabaseHelper.COLUMN_CATEGORY_SOUND_ID + " = n." + DatabaseHelper.COLUMN_SOUND_ID;

        try (Cursor cursor = database.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                Category category = cursorToCategory(cursor);
                categories.add(category);
            }
        }

        return categories;
    }

    public void close() {
        dbHelper.close();
    }
}
