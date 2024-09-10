package com.example.personalschedulemanagementapp.ui.sound;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentSoundBinding;
import com.example.personalschedulemanagementapp.entity.Sound;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SoundFragment extends Fragment {

    private static final int PICK_SOUND_REQUEST = 1;

    private FragmentSoundBinding binding;
    private ListView listViewSounds;
    private List<Sound> sounds;
    private SoundDAO soundDAO;
    private SoundAdapter soundAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewSounds = binding.listViewSounds;

        soundDAO = new SoundDAO(requireContext());
        sounds = soundDAO.getAllSounds();

        soundAdapter = new SoundAdapter(requireContext(), sounds, sound -> {
            soundDAO.deleteSound(sound.getId());
            updateSoundList();
        });

        listViewSounds.setAdapter(soundAdapter);

        FloatingActionButton fabAddSound = binding.fabAddSound;
        fabAddSound.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_SOUND_REQUEST);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SOUND_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri soundUri = data.getData();
            if (soundUri != null) {
                String soundName = getFileName(soundUri);
                int soundId = saveSoundToStorage(soundUri);
                if (soundId != 0) {
                    Sound sound = new Sound(soundName, soundId);
                    soundDAO.insertOrUpdateSound(sound);
                    updateSoundList();
                }
            }
        }
    }

    private int saveSoundToStorage(Uri soundUri) {
        int fileId = 0;
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(soundUri);
            if (inputStream != null) {
                File soundFile = new File(requireContext().getFilesDir(), getFileName(soundUri));
                FileOutputStream outputStream = new FileOutputStream(soundFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.close();
                inputStream.close();

                fileId = soundFile.hashCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileId;
    }

    private String getFileName(Uri uri) {
        String fileName = "";
        Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }

    private void updateSoundList() {
        sounds = soundDAO.getAllSounds();
        soundAdapter.clear();
        soundAdapter.addAll(sounds);
        soundAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (soundDAO != null) {
            soundDAO.close();
        }
        binding = null;
    }
}