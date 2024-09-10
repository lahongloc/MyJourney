package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.dao.CategoryDAO;
import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.RemindTime;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.Sound;
import com.example.personalschedulemanagementapp.entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private TextInputEditText etCategoryName;
    private TextInputEditText etCategoryDescription;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        etCategoryName = findViewById(R.id.categoryNameInput);
        etCategoryDescription = findViewById(R.id.categoryDescriptionInput);
        MaterialButton addCategoryButton = findViewById(R.id.addButton);

        int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        CategoryDAO categoryDAO = new CategoryDAO(this);
        SoundDAO soundDAO = new SoundDAO(this);

        if (categoryId == -1) {
            category = new Category();
            addCategoryButton.setText("Create Category");
        } else {
            category = categoryDAO.getCategoryById(this, categoryId);
            if (category != null) {
                etCategoryName.setText(category.getName());
                etCategoryDescription.setText(category.getDescription());
                addCategoryButton.setText("Update Category");
            } else {
                finish(); // Nếu không tìm thấy category, thoát activity
                return;
            }
        }

        // Set remind time
        List<RemindTime> remindTimes = new ArrayList<>();
        remindTimes.add(new RemindTime("3 minutes", 3));
        remindTimes.add(new RemindTime("15 minutes", 15));
        remindTimes.add(new RemindTime("30 minutes", 30));
        remindTimes.add(new RemindTime("1 hour", 60));

        Spinner remindTimeSpinner = findViewById(R.id.remindTimeSpinner);
        ArrayAdapter<RemindTime> remindTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, remindTimes);
        remindTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindTimeSpinner.setAdapter(remindTimeAdapter);

        // Set value for Spinner
        if (categoryId != -1) {
            for (int i = 0; i < remindTimes.size(); i++) {
                if (remindTimes.get(i).getValue() == category.getRemindTime()) {
                    remindTimeSpinner.setSelection(i);
                    break;
                }
            }
        }

        remindTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                RemindTime selectedRemindTime = (RemindTime) parentView.getSelectedItem();
                category.setRemindTime(selectedRemindTime.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing or set default reminder time
            }
        });

        // Set sound
        List<Sound> sounds = soundDAO.getAllSounds();
        Spinner soundSpinner = findViewById(R.id.soundSpinner);
        ArrayAdapter<Sound> soundAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sounds);
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundSpinner.setAdapter(soundAdapter);

        // Set value for Spinner
        if (categoryId != -1 && category.getSound() != null) {
            for (int i = 0; i < sounds.size(); i++) {
                if (sounds.get(i).getUri().equals(category.getSound().getUri())) {
                    soundSpinner.setSelection(i);
                    break;
                }
            }
        }

        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Sound selectedSound = (Sound) parentView.getSelectedItem();
                SoundHelper soundHelper = new SoundHelper();
                soundHelper.playNotificationSound(CategoryActivity.this, selectedSound.getUri());
                category.setSound(selectedSound);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing or set default sound
            }
        });

        // Handle add/update button click
        addCategoryButton.setOnClickListener(view -> {
            String categoryName = Objects.requireNonNull(etCategoryName.getText()).toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoryDescription = Objects.requireNonNull(etCategoryDescription.getText()).toString().trim();
            category.setName(categoryName);
            category.setDescription(categoryDescription);

            // Save category to database
            categoryDAO.insertOrUpdateCategory(category);

            if (categoryId == -1) {
                Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
            }

            // Navigate based on role
            User user = UserManager.getInstance().getUser();
            Intent intent;
            if (Role.ADMIN.name().equals(user.getRole())) {
                intent = new Intent(CategoryActivity.this, AdminActivity.class);
            } else {
                intent = new Intent(CategoryActivity.this, UserActivity.class);
            }
            startActivity(intent);
            finish(); // Finish activity to prevent returning to this screen
        });
    }
}
