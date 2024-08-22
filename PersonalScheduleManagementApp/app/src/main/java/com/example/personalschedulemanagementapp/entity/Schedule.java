package com.example.personalschedulemanagementapp.entity;

import java.util.Calendar;

public class Schedule {
    private int id;
    private String sounds;
    private String title;
    private String description;
    private Calendar time;
    private String status;
    private int categoryId;

    public Schedule() {}

    public Schedule(int id, String sounds, String title, String description, Calendar time, String status, int categoryId) {
        this.id = id;
        this.sounds = sounds;
        this.title = title;
        this.description = description;
        this.time = time;
        this.status = status;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSounds() {
        return sounds;
    }

    public void setSounds(String sounds) {
        this.sounds = sounds;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
