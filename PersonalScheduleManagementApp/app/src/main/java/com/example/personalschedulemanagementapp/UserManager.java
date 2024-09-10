package com.example.personalschedulemanagementapp;

import com.example.personalschedulemanagementapp.entity.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public int getUserId() {
        return currentUser != null ? (int) currentUser.getId() : -1;
    }
}
