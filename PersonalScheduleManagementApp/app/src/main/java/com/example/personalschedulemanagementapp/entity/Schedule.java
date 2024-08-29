package com.example.personalschedulemanagementapp.entity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.personalschedulemanagementapp.AlarmReceiver;

import java.util.Calendar;

public class Schedule {
    private int id;
    private String title;
    private String description;
    private Calendar time;
    private String status;
    private Category category;

    public Schedule() {}

    public Schedule(int id, String title, String description, Calendar time, String status, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.status = status;
        this.category = category;
    }

    public void setAlarmForSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("SCHEDULE_ID", this.id);

        long intervalMillis = 5 * 1000;
        long firstAlarmTime = this.getTime().getTimeInMillis() - ((long) getCategory().getRemindTime() * 60 * 1000);
        for (int i = 0; i < 5; i++) {
            long alarmTime = firstAlarmTime + (i * intervalMillis);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), this.id + i, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    public void updateAlarms(Context context) {
        cancelAlarmsForSchedule(context);
        setAlarmForSchedule(context);
    }

    public void cancelAlarmsForSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("SCHEDULE_ID", this.id);

        for (int i = 0; i < 5; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), this.id + i, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
