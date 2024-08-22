package com.example.personalschedulemanagementapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.entity.Sound;

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

        List<Sound> sounds = new ArrayList<>();
        sounds.add(new Sound("Sci-fi Confirmation", R.raw.mixkit_sci_fi_confirmation_914));
        sounds.add(new Sound("DoorBell Tone", R.raw.mixkit_doorbell_tone_2864));
        sounds.add(new Sound("Software Interface Remove", R.raw.mixkit_software_interface_remove_2576));

        sounds.forEach(sound -> {
            if (Objects.isNull(soundDAO.getSoundBySoundId(sound.getSoundId()))) {
                soundDAO.insertOrUpdateSound(sound);
            }
        });
    }
}