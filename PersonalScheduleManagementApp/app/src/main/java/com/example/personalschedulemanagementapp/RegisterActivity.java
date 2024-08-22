package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.dao.UserDAO;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        EditText edfullName = findViewById(R.id.etFullName);
        EditText edusername = findViewById(R.id.etUsername);
        EditText edemail = findViewById(R.id.etEmail);
        EditText edpassword = findViewById(R.id.etPassword);

        Button registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(view -> {
            String fullName = edfullName.getText().toString().trim();
            String username = edusername.getText().toString().trim();
            String email = edemail.getText().toString().trim();
            String password = edpassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty()
                    || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                UserDAO userDAO = new UserDAO(this);
                long userId = userDAO.insertUser(username, password, fullName, email, Role.USER.name());

                if (userId > 0) {
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}