package com.example.personalschedulemanagementapp.entity;


import android.net.Uri;

import androidx.annotation.NonNull;


public class Sound {
    private int id;
    private String name;
    private Uri uri;


    public Sound() {
    }

    public Sound(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public Sound(int id, String name, Uri uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
