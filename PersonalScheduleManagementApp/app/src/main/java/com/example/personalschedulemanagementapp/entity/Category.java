package com.example.personalschedulemanagementapp.entity;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Category {
    private int id;
    private String name;
    private String description;
    private int remindTime;
    private Sound sound;

    public Category() {};

    public Category(int id, String name, String description, int remindTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.remindTime = remindTime;
    }

    public Category(int id, String name, String description, int remindTime, Sound sound) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.remindTime = remindTime;
        this.sound = sound;
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

    public int getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(int remindTime) {
        this.remindTime = remindTime;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
