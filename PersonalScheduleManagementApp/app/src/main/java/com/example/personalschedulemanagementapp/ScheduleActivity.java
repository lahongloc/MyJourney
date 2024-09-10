package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.dao.CategoryDAO;
import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.Status;
import com.example.personalschedulemanagementapp.entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity {

    private Schedule schedule;
    private TextInputEditText timeInput;
    private Calendar date;
    private TextInputEditText etScheduleTitle;
    private TextInputEditText etScheduleDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        etScheduleTitle = findViewById(R.id.scheduleTitleInput);
        etScheduleDescription = findViewById(R.id.scheduleDescriptionInput);
        MaterialButton addScheduleButton = findViewById(R.id.addScheduleButton);

        int scheduleId = getIntent().getIntExtra("SCHEDULE_ID", -1);
        if (scheduleId == -1) {
            schedule = new Schedule();
            addScheduleButton.setText("Create Schedule");

            if (UserManager.getInstance().getUserId() != -1) {
                schedule.setUser(UserManager.getInstance().getUser());
            }

        } else {
            ScheduleDAO scheduleDAO = new ScheduleDAO(this);
            schedule = scheduleDAO.getScheduleById(scheduleId, this);
            etScheduleTitle.setText(schedule.getTitle());
            etScheduleDescription.setText(schedule.getDescription());
            addScheduleButton.setText("Update Schedule");
        }

        // Khởi tạo timeInput từ layout
        timeInput = findViewById(R.id.scheduleTimeInput);
        date = Calendar.getInstance();

        // Đặt sự kiện click cho timeInput
        timeInput.setOnClickListener(v -> showDatePicker());

        if (scheduleId != -1) {
            String selectedDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(schedule.getTime().getTime());
            timeInput.setText(selectedDateTime);
        }

        // Khởi tạo list category
        CategoryDAO categoryDAO = new CategoryDAO(this);
        List<Category> categories = categoryDAO.getAllCategories(this);

        Spinner scheduleCategorySpinner = findViewById(R.id.scheduleCategorySpinner);
        ArrayAdapter<Category> scheduleCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        scheduleCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheduleCategorySpinner.setAdapter(scheduleCategoryAdapter);

        // Đặt giá trị đã chọn cho Spinner
        if (scheduleId != -1) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == schedule.getCategory().getId()) {
                    scheduleCategorySpinner.setSelection(i);
                    break;
                }
            }
        }

        // Đặt listener cho spinner để lưu category đã chọn
        scheduleCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                schedule.setCategory((Category) parentView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                schedule.setCategory((Category) parentView.getSelectedItem());
            }
        });

        // Xử lý nút add
        addScheduleButton.setOnClickListener(view -> {
//            private TextInputEditText etScheduleTitle = findViewById(R.id.scheduleTitleInput);
            String scheduleTitle = Objects.requireNonNull(etScheduleTitle.getText()).toString().trim();
            if (scheduleTitle.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập title", Toast.LENGTH_SHORT).show();
                return;
            }

//            TextInputEditText etScheduleDescription = findViewById(R.id.scheduleDescriptionInput);
            String scheduleDescription = Objects.requireNonNull(etScheduleDescription.getText()).toString().trim();

            if (schedule.getCategory() == null) {
                Toast.makeText(this, "Vui lòng chọn category", Toast.LENGTH_SHORT).show();
                return;
            }
            ScheduleDAO scheduleDAO = new ScheduleDAO(this);
            if (scheduleDAO.getScheduleTitleByTime(date) != null) {
                Toast.makeText(this, "Lịch trình dã bị trùng với " + scheduleDAO.getScheduleTitleByTime(date), Toast.LENGTH_SHORT).show();
                return;
            }

            // Thiết lập các giá trị cho Schedule

            schedule.setTitle(scheduleTitle);
            schedule.setDescription(scheduleDescription);
            schedule.setTime(date);
            schedule.setStatus(Status.WAITING.name());

            // Lưu schedule vào cơ sở dữ liệu
            int id = (int) scheduleDAO.insertOrUpdateSchedule(schedule);

            if (scheduleId == -1) {
                Schedule scheduleInserted = scheduleDAO.getScheduleById(id, this);
                scheduleInserted.setAlarmForSchedule(this);
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                schedule.updateAlarms(this);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }

            User user = UserManager.getInstance().getUser();
            if (user.getRole().equals(Role.ADMIN.name())) {
                Intent intent = new Intent(ScheduleActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ScheduleActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date");
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            date.setTimeInMillis(selection);
            showTimePicker();
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void showTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(date.get(Calendar.HOUR_OF_DAY))
                .setMinute(date.get(Calendar.MINUTE))
                .setTitleText("Select Time")
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            date.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            date.set(Calendar.MINUTE, timePicker.getMinute());
            String selectedDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(date.getTime());
            timeInput.setText(selectedDateTime);
        });

        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NotificationPermissionHelper.REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
                Toast.makeText(this, "Quyền thông báo đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Quyền thông báo bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
