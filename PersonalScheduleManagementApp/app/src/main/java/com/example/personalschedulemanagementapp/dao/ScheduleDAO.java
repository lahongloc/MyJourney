package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ScheduleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert a new schedule
    public void insertOrUpdateSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID, schedule.getCategory().getId());
//        values.put(DatabaseHelper.COLUMN_SCHEDULE_SOUND, sound);
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION, schedule.getDescription());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TIME, schedule.getTime().getTimeInMillis());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_STATUS, schedule.getStatus());

        if (schedule.getId() > 0) {
            String selection = DatabaseHelper.COLUMN_SCHEDULE_ID + " = ?";
            String[] selectionArgs = { String.valueOf(schedule.getId()) };

            database.update(DatabaseHelper.TABLE_SCHEDULE, values, selection, selectionArgs);
        } else {
            database.insert(DatabaseHelper.TABLE_SCHEDULE, null, values);
        }
    }

    // Retrieve all schedules as List<Schedule>
    public List<Schedule> getAllSchedules(Context context) {
        List<Schedule> schedules = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_SCHEDULE, columns, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Schedule schedule = cursorToSchedule(cursor, context);
                schedules.add(schedule);
            }
        }

        return schedules;
    }

    // Retrieve schedules by categoryId as List<Schedule>
    public List<Schedule> getSchedulesByCategoryId(int categoryId, Context context) {
        List<Schedule> schedules = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_SCHEDULE, columns, DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)}, null, null, null)) {
            while (cursor.moveToNext()) {
                Schedule schedule = cursorToSchedule(cursor, context);
                schedules.add(schedule);
            }
        }

        return schedules;
    }

    // Retrieve a specific schedule by ID
    public Schedule getScheduleById(int scheduleId, Context context) {
        Schedule schedule = null;
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_SCHEDULE, columns, DatabaseHelper.COLUMN_SCHEDULE_ID + " = ?", new String[]{String.valueOf(scheduleId)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                schedule = cursorToSchedule(cursor, context);
            }
        }

        return schedule;
    }

    // Convert Cursor to Schedule
    private Schedule cursorToSchedule(Cursor cursor, Context context) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_ID));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION));
        long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TIME));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_STATUS));

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeMillis);

        // You may want to retrieve the Category based on the categoryId if needed
        // For simplicity, assuming a method getCategoryById in CategoryDAO
        Category category = new CategoryDAO(context).getCategoryById(categoryId);


        // Returning a new Schedule object
        return new Schedule(id, title, description, time, status, category); // You can set the category as needed
    }

    // Delete a schedule
    public int deleteSchedule(int scheduleId) {
        String selection = DatabaseHelper.COLUMN_SCHEDULE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(scheduleId) };

        return database.delete(DatabaseHelper.TABLE_SCHEDULE, selection, selectionArgs);
    }

    public void close() {
        dbHelper.close();
    }
}
