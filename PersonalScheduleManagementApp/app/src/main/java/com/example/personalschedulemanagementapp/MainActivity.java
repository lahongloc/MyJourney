package com.example.personalschedulemanagementapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.dao.CategoryDAO;
import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.entity.Category;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.Sound;
import com.example.personalschedulemanagementapp.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SoundDAO soundDAO = new SoundDAO(this);
        CategoryDAO categoryDAO = new CategoryDAO(this);

        List<Sound> sounds = new ArrayList<>();
        sounds.add(new Sound("Sci-fi Confirmation", R.raw.mixkit_sci_fi_confirmation_914));
        sounds.add(new Sound("DoorBell Tone", R.raw.mixkit_doorbell_tone_2864));
        sounds.add(new Sound("Software Interface Remove", R.raw.mixkit_software_interface_remove_2576));

        sounds.forEach(sound -> {
            if (Objects.isNull(soundDAO.getSoundBySoundId(sound.getSoundId()))) {
                soundDAO.insertOrUpdateSound(sound);
            }
        });

//        List<Category> categories = new ArrayList<>();
//        categories.add(new Category("The thao", "loai the thao", 30));
//        categories.add(new Category("Cong viec", "loai cong viec", 10));
//        categories.forEach(category -> {
//            if(Objects.isNull(categoryDAO.getCategoryById(category.getId()))) {
//                categoryDAO.insertOrUpdateCategory(category);
//            }
//        });

        UserDAO userDAO = new UserDAO(this);
        User admin = new User();
        admin.setFullName("Administrator");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN.name());
        long success = userDAO.insertUser(admin);
    }
}