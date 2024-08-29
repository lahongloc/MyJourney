package com.example.personalschedulemanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private FloatingActionButton fabAddUser;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        fabAddUser = findViewById(R.id.fabAddUser);

        // Assume you have a method to get the list of users
        UserDAO userDAO = new UserDAO(this);
        userList = userDAO.getUsers();

        userAdapter = new UserAdapter(this, userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEditClicked(User user) {
                // Handle edit user logic here
            }

            @Override
            public void onDeleteClicked(User user) {
                // Handle delete user logic here
            }
        });

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add user logic here, for example start a new activity
                Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
                UserListActivity.this.startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        UserDAO userDAO = new UserDAO(this);
        // Refresh the user list when the activity resumes
        userList.clear();
        userList.addAll(userDAO.getUsers());
        userAdapter.notifyDataSetChanged();
    }
}