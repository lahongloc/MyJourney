package com.example.personalschedulemanagementapp.entity;

import java.util.Calendar;

public class Category {
    private int id;
    private String name;
    private String description;
    private Calendar remindTime;
    private int notificationId;

    public Category() {};

    public Category(int id, String name, String description, Calendar remindTime, int notificationId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.remindTime = remindTime;
        this.notificationId = notificationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Calendar remindTime) {
        this.remindTime = remindTime;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
