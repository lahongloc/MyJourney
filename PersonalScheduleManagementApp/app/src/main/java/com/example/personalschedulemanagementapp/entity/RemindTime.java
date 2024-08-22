package com.example.personalschedulemanagementapp.entity;

import androidx.annotation.NonNull;

public class RemindTime {
    private String name;
    private int value;

    public RemindTime(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
