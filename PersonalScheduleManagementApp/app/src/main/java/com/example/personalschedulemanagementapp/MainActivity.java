package com.example.personalschedulemanagementapp;

import android.os.Bundle;
import android.database.Cursor;

import com.example.personalschedulemanagementapp.dao.CategoryDAO;
import com.example.personalschedulemanagementapp.dao.NotificationDAO;
import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.User;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.personalschedulemanagementapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private NotificationDAO notificationDAO;
    private CategoryDAO categoryDAO;
    private ScheduleDAO scheduleDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        this.userDAO = new UserDAO(this);
//        long newUserId = userDAO.insertUser("john_doe", "password123", "John Doe", "john@example.com", Role.ADMIN.name());
        displayUsers();


//        this.notificationDAO = new NotificationDAO(this);
//        long newNotificationId = notificationDAO.insertNotification("Đi chơi");
        displayNotifications();

//        this.categoryDAO = new CategoryDAO(this);
//        long newCategoryId = categoryDAO.insertCategory((int) newNotificationId, "Thông báo gấp", "Đây là thông báo gấp, trước 30'!", "30 phút");
        displayCategories();
    }

    private void displayUsers() {
        UserDAO userDAO = new UserDAO(this);
        List<User> users = userDAO.getUsers();

        StringBuilder builder = new StringBuilder();

        users.forEach(u -> builder.append(u.getUsername()).append("\n"));


        // Assuming you have a TextView with the ID 'textViewUsers' in your layout
        TextView textViewUsers = findViewById(R.id.textView);
        textViewUsers.setText(builder.toString());
    }

    private void displayNotifications() {
        NotificationDAO notificationDAO = new NotificationDAO(this);
        Cursor cursor = notificationDAO.getAllNotifications();
        StringBuilder builder = new StringBuilder();

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_ID));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_CONTENT));
            builder.append("ID: ").append(id).append(", content: ").append(content).append("\n");
        }

        cursor.close();

        TextView txt = findViewById(R.id.textView2);
        txt.setText(builder.toString());
    }


    private void displayCategories() {
        CategoryDAO categoryDAO = new CategoryDAO(this);
        Cursor cursor = categoryDAO.getAllCategoriesWithNotifications();
        StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));
//            String notificationId = "haha";
//            int notificationId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NOTIFICATION_ID.toString()));
            String name = cursor.getString((cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION));
            String remindTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_REMIND_TIME));
            String notificationContent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTIFICATION_CONTENT));

            builder.append("ID: ").append(id).append(", notificationId: ")
                    .append(notificationContent != null ? notificationContent : "No notification linked").append(", name: ").append(name).append(", description: ").append(description).append(", remind time: ").append(remindTime).append("\n");
        }

        TextView txt = findViewById(R.id.textView2);
        txt.setText(builder.toString());
    }
}