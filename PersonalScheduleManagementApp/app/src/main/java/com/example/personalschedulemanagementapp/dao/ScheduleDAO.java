package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.Status;
import com.example.personalschedulemanagementapp.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ScheduleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public List<Schedule> getSchedulesByUserId(int userId, Context context) {
        List<Schedule> schedules = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID,
                DatabaseHelper.COLUMN_SCHEDULE_USER_ID,
                DatabaseHelper.COLUMN_SCHEDULE_TITLE,
                DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION,
                DatabaseHelper.COLUMN_SCHEDULE_TIME,
                DatabaseHelper.COLUMN_SCHEDULE_STATUS
        };

        String selection = DatabaseHelper.COLUMN_SCHEDULE_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_SCHEDULE, columns, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                Schedule schedule = cursorToSchedule(cursor, context);
                schedules.add(schedule);
            }
        }

        return schedules;
    }


    public Map<String, Integer> getScheduleCountByCategory() {
        Map<String, Integer> categoryCountMap = new HashMap<>();

        String query = "SELECT Category.name AS category, COUNT(*) AS count " +
                "FROM " + DatabaseHelper.TABLE_SCHEDULE + " AS Schedule " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY + " AS Category " +
                "ON Schedule.categoryId = Category.id " +
                "GROUP BY Category.name";

        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            categoryCountMap.put(category, count);
        }

        cursor.close();
        return categoryCountMap;
    }

    public Calendar getDateFromInteger(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }

    public long getIntegerFromDate(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    public Map<String, Integer> getScheduleCountByDateRange(Calendar startDate, Calendar endDate) {
        Map<String, Integer> countMap = new HashMap<>();

        long startTimeMillis = startDate.getTimeInMillis();
        long endTimeMillis = endDate.getTimeInMillis();

        String query = "SELECT time FROM " + DatabaseHelper.TABLE_SCHEDULE
                + " WHERE time BETWEEN ? AND ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(startTimeMillis), String.valueOf(endTimeMillis)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long time = cursor.getLong(cursor.getColumnIndexOrThrow("time"));
                // Chuyển đổi lại thời gian từ mili giây thành định dạng ngày tháng
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());

                // Đếm số lượng lịch trình cho từng ngày
                countMap.put(date, countMap.getOrDefault(date, 0) + 1);
            }
            cursor.close();
        }

        return countMap;
    }

    public Map<String, Integer> getScheduleCountByHourOfDate(Calendar date) {
        Map<String, Integer> countMap = new HashMap<>();

        // Xác định khoảng thời gian bắt đầu và kết thúc của ngày
        Calendar startOfDay = (Calendar) date.clone();
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);

        Calendar endOfDay = (Calendar) date.clone();
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);

        long startTimeMillis = startOfDay.getTimeInMillis();
        long endTimeMillis = endOfDay.getTimeInMillis();

        // Truy vấn SQL
        String query = "SELECT strftime('%H:%M', time / 1000, 'unixepoch') AS hour, COUNT(*) AS count " +
                "FROM " + DatabaseHelper.TABLE_SCHEDULE +
                " WHERE time BETWEEN ? AND ?" +
                " GROUP BY hour";

        // Thực thi truy vấn
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(startTimeMillis), String.valueOf(endTimeMillis)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                countMap.put(hour, count);
            }
            cursor.close();
        }

        return countMap;
    }



    // Insert a new schedule
    public long insertOrUpdateSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID, schedule.getCategory().getId());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_USER_ID, schedule.getUser().getId());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION, schedule.getDescription());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_TIME, schedule.getTime().getTimeInMillis());
        values.put(DatabaseHelper.COLUMN_SCHEDULE_STATUS, schedule.getStatus());

        if (schedule.getId() > 0) {
            String selection = DatabaseHelper.COLUMN_SCHEDULE_ID + " = ?";
            String[] selectionArgs = { String.valueOf(schedule.getId()) };

            return database.update(DatabaseHelper.TABLE_SCHEDULE, values, selection, selectionArgs);
        } else {
            return database.insert(DatabaseHelper.TABLE_SCHEDULE, null, values);
        }
    }


    // Retrieve all schedules as List<Schedule>
    public List<Schedule> getAllSchedules(Context context) {
        List<Schedule> schedules = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_SCHEDULE_ID,
                DatabaseHelper.COLUMN_SCHEDULE_CATEGORY_ID,
                DatabaseHelper.COLUMN_SCHEDULE_USER_ID,
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

    public String getScheduleTitleByTime(Calendar calendar) {
        long timeInMillis = calendar.getTimeInMillis();
        String[] columns = { DatabaseHelper.COLUMN_SCHEDULE_TITLE };
        String selection = DatabaseHelper.COLUMN_SCHEDULE_TIME + " = ? AND " +
                DatabaseHelper.COLUMN_SCHEDULE_STATUS + " = ?";
        String[] selectionArgs = { String.valueOf(timeInMillis), Status.WAITING.name() };

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_SCHEDULE, columns, selection, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TITLE));
            }
        }

        return null; // Trả về null nếu không tìm thấy lịch trình phù hợp
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
                DatabaseHelper.COLUMN_SCHEDULE_USER_ID,
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
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_USER_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DESCRIPTION));
        long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_TIME));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_STATUS));

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeMillis);

        Category category = new CategoryDAO(context).getCategoryById(categoryId);
        User user = new UserDAO(context).getUserById(userId);

        return new Schedule(id, title, description, time, status, category, user);
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
