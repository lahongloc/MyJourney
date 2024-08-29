package com.example.personalschedulemanagementapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.entity.Sound;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SoundListActivity extends AppCompatActivity {

    private static final int PICK_SOUND_REQUEST = 1;

    private SoundDAO soundDAO;
    private SoundListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_list);

        soundDAO = new SoundDAO(this);

        ListView listViewSounds = findViewById(R.id.listViewSounds);

        List<Sound> sounds = soundDAO.getAllSounds();
        adapter = new SoundListAdapter(this, sounds, sound -> {
            soundDAO.deleteSound(sound.getId());
            updateSoundList();
        });
        listViewSounds.setAdapter(adapter);

        FloatingActionButton fabAddSound = findViewById(R.id.fabAddSound);
        fabAddSound.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_SOUND_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SOUND_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri soundUri = data.getData();
            if (soundUri != null) {
                String soundName = getFileName(soundUri);

                Sound sound = new Sound(soundName, saveSoundToStorage(soundUri));
                soundDAO.insertOrUpdateSound(sound);
                updateSoundList();
            }
        }
    }

    private int saveSoundToStorage(Uri soundUri) {
        int fileId = 0; // Default value if an error occurs
        try {
            // Mở InputStream từ Uri
            InputStream inputStream = getContentResolver().openInputStream(soundUri);

            // Tạo một tệp mới trong thư mục lưu trữ
            File soundFile = new File(getFilesDir(), getFileName(soundUri));
            FileOutputStream outputStream = new FileOutputStream(soundFile);

            // Sao chép nội dung từ InputStream vào OutputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Đóng các luồng
            outputStream.close();
            inputStream.close();

            // Trả về mã hash của tệp như là ID của âm thanh
            fileId = soundFile.hashCode(); // You can use another method to generate a unique ID
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileId;
    }


    private String getFileName(Uri uri) {
        String fileName = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        }
        return fileName;
    }

//    private int saveSoundToStorage(Uri uri) {
//        // Implementation to save the sound file to storage and return a soundId.
//        // You can save the file to internal/external storage and use the path or some identifier as soundId.
//        return 0; // Replace with actual soundId after saving the file.
//    }

    private void updateSoundList() {
        List<Sound> updatedSounds = soundDAO.getAllSounds();
        adapter.clear();
        adapter.addAll(updatedSounds);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundDAO.close();
    }
}
