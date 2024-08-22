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
import com.example.personalschedulemanagementapp.entity.Sound;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private TextInputEditText etCategoryName;
    private TextInputEditText etCategoryDescription;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        etCategoryName = findViewById(R.id.categoryNameInput);
        etCategoryDescription = findViewById(R.id.categoryDescriptionInput);
        MaterialButton addCategoryButton = findViewById(R.id.addButton);

        int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        if (categoryId == -1) {
            category = new Category();
            addCategoryButton.setText("Create Category");
        } else {
            CategoryDAO categoryDAO = new CategoryDAO(this);
            category = categoryDAO.getCategoryById(categoryId);
            etCategoryName.setText(category.getName());
            etCategoryDescription.setText(category.getDescription());
            addCategoryButton.setText("Update Category");
        }

        // set remind time
        List<RemindTime> remindTimes = new ArrayList<>();
        remindTimes.add(new RemindTime("15 minutes", 15));
        remindTimes.add(new RemindTime("30 minutes", 30));
        remindTimes.add(new RemindTime("1 hours", 60));

        Spinner remindTimeSpinner = findViewById(R.id.remindTimeSpinner);
        ArrayAdapter<RemindTime> remindTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, remindTimes);
        remindTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindTimeSpinner.setAdapter(remindTimeAdapter);

        // Đặt giá trị đã chọn cho Spinner
        if (categoryId != -1) {
            for (int i = 0; i < remindTimes.size(); i++) {
                if (remindTimes.get(i).getValue() == category.getRemindTime()) {
                    remindTimeSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Đặt listener nếu cần
        remindTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                RemindTime selectedRemindTime = (RemindTime) parentView.getSelectedItem();
                category.setRemindTime(selectedRemindTime.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                RemindTime selectedRemindTime = (RemindTime) parentView.getSelectedItem();
                category.setRemindTime(selectedRemindTime.getValue());
            }
        });

        // select sound
        SoundDAO soundDAO = new SoundDAO(this);
        List<Sound> sounds = soundDAO.getAllSounds();

        Spinner soundSpinner = findViewById(R.id.soundSpinner);
        ArrayAdapter<Sound> soundAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sounds);
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundSpinner.setAdapter(soundAdapter);

        // Đặt giá trị đã chọn cho Spinner
        if (categoryId != -1) {
            for (int i = 0; i < sounds.size(); i++) {
                if (sounds.get(i).getSoundId() == category.getSound().getSoundId()) {
                    soundSpinner.setSelection(i);
                    break;
                }
            }
        }

        // Đặt listener nếu cần
        soundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Sound selectedSound = (Sound) parentView.getSelectedItem();
                SoundHelper soundHelper = new SoundHelper();
                soundHelper.playNotificationSound(CategoryActivity.this, selectedSound.getSoundId());
                category.setSound(selectedSound);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không chọn mục nào
                Sound selectedSound = (Sound) parentView.getSelectedItem();
                category.setSound(selectedSound);
            }
        });

        // xử lý nút add
        addCategoryButton.setOnClickListener(view -> {
            String categoryName = Objects.requireNonNull(etCategoryName.getText()).toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoryDescription = Objects.requireNonNull(etCategoryDescription.getText()).toString().trim();
            category.setName(categoryName);
            category.setDescription(categoryDescription);

            // Lưu schedule vào cơ sở dữ liệu
            CategoryDAO categoryDAO = new CategoryDAO(this);
            categoryDAO.insertOrUpdateCategory(category);

            if (categoryId == -1)
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CategoryActivity.this, UserActivity.class);
            startActivity(intent);
        });
    }
}