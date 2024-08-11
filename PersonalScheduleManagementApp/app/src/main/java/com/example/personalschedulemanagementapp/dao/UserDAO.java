package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.User;

public class UserDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insertUser(String username, String password, String fullname, String email, String role) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_USER_FULLNAME, fullname);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_ROLE, role);
        return database.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    public Cursor getAllUsers() {
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE
        };
        return database.query(DatabaseHelper.TABLE_USER, columns, null, null, null, null, null);
    }

    public User getUserByUsername(String username, String password) {
        String[] columns = {
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_USERNAME,
                DatabaseHelper.COLUMN_USER_PASSWORD,
                DatabaseHelper.COLUMN_USER_FULLNAME,
                DatabaseHelper.COLUMN_USER_EMAIL,
                DatabaseHelper.COLUMN_USER_ROLE
        };
        String selection = DatabaseHelper.COLUMN_USER_USERNAME + " = ? AND "
                + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?;";
        String[] selectionArgs = { username, password };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USER,   // Bảng cần truy vấn
                columns,                     // Các cột cần lấy
                selection,                   // Điều kiện WHERE
                selectionArgs,               // Giá trị tương ứng với điều kiện WHERE
                null,                        // groupBy
                null,                        // having
                null                         // orderBy
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Tạo đối tượng User từ dữ liệu trong Cursor
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE))
            );
            cursor.close(); // Đóng cursor sau khi sử dụng xong
            return user;
        } else {
            cursor.close(); // Đóng cursor nếu không tìm thấy kết quả
            return null; // Trả về null nếu không tìm thấy user nào
        }
    }

    public void close() {
        dbHelper.close();
    }
}
