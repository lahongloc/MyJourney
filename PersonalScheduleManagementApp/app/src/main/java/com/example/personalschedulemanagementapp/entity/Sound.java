package com.example.personalschedulemanagementapp.entity;


import androidx.annotation.NonNull;


public class Sound {
    private int id;
    private String name;
    private int soundId;


    public Sound() {
    }

    public Sound(String name, int soundId) {
        this.name = name;
        this.soundId = soundId;
    }

    public Sound(int id, String name, int soundId) {
        this.id = id;
        this.name = name;
        this.soundId = soundId;
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

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
