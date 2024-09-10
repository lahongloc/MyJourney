package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Chuyển sang Register View
        Button registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Login
        EditText edUsername = findViewById(R.id.username);
        EditText edPassword = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String username = edUsername.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty())
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            else {
                UserDAO userDAO = new UserDAO(this);
                User user = userDAO.getUserByUsername(username, password);
                UserManager.getInstance().setUser(user);

                if (Objects.isNull(user)) {
                    Toast.makeText(this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if (user.getRole().equals(Role.ADMIN.name())) {
                        Intent intent = new Intent(this, AdminActivity.class);
                        startActivity(intent);
                    } else if (user.getRole().equals(Role.USER.name())) {
                        Intent intent = new Intent(this, UserActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}