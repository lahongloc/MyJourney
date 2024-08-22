package com.example.personalschedulemanagementapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.entity.User;

public class EditUserActivity extends AppCompatActivity {

    private Context context;
    private EditText editTextUsername, editTextFullName, editTextEmail, editTextRole;
    private int userId;
    private UserDAO userDAO;
    private User user;

    private Button cancelButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextRole = findViewById(R.id.editTextRole);

        userDAO = new UserDAO(this);

        cancelButton = findViewById(R.id.buttonCancel);
        saveButton = findViewById(R.id.buttonSave);
//
//        // Get the user ID from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username"); // Use only the key
        String password = intent.getStringExtra("password"); // Use only the key
//
        this.user = userDAO.getUserByUsername(username, password);
//
        if (user != null) {
            editTextUsername.setText(user.getUsername());
            editTextFullName.setText(user.getFullName());
            editTextEmail.setText(user.getEmail());
            editTextRole.setText(user.getRole());
        }

        cancelButton.setOnClickListener(view -> {
                Intent userListIntent = new Intent(this, UserListActivity.class);
                startActivity(userListIntent);
        });

        saveButton.setOnClickListener(view -> {
            this.user.setUsername(editTextUsername.getText().toString());
            this.user.setFullName(editTextFullName.getText().toString());
            this.user.setEmail(editTextEmail.getText().toString());
            this.user.setRole(editTextRole.getText().toString());

            long result = this.userDAO.updateUser(this.user);
            if (result > 0) {
                // Update was successful
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Update failed
                Toast.makeText(this, "User update failed", Toast.LENGTH_SHORT).show();
            }
            cancelButton.callOnClick();

//            this.userDAO.
        });
    }
}