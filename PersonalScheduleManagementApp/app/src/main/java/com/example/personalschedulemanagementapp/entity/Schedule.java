package com.example.personalschedulemanagementapp.entity;

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
