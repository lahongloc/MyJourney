package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.dao.UserDAO;

public class AddUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user); // Updated to the new layout file

        // Initialize views with updated IDs
        EditText edFullName = findViewById(R.id.editTextFullName);
        EditText edUsername = findViewById(R.id.editTextUsername);
        EditText edEmail = findViewById(R.id.editTextEmail);
        EditText edPassword = findViewById(R.id.editTextPassword);

        Button registerButton = findViewById(R.id.buttonRegister);
        Button cancelButton = findViewById(R.id.buttonCancel);

        registerButton.setOnClickListener(view -> {
            String fullName = edFullName.getText().toString().trim();
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            } else {
                UserDAO userDAO = new UserDAO(this);
                long userId = userDAO.insertUser(username, password, fullName, email, Role.USER.name());

                if (userId > 0) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    navigateToUserListActivity();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(view -> {
            navigateToUserListActivity();
        });
    }

    private void navigateToUserListActivity() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
}
