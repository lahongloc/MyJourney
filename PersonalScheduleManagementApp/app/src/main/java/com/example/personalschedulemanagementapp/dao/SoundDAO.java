package com.example.personalschedulemanagementapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalschedulemanagementapp.data.DatabaseHelper;
import com.example.personalschedulemanagementapp.entity.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public SoundDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Insert or update a sound
    public void insertOrUpdateSound(Sound sound) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SOUND_NAME, sound.getName());
        values.put(DatabaseHelper.COLUMN_SOUND_SOUNDID, sound.getSoundId());

        if (sound.getId() > 0) {
            String selection = DatabaseHelper.COLUMN_SOUND_ID + " = ?";
            String[] selectionArgs = { String.valueOf(sound.getId()) };
            database.update(DatabaseHelper.TABLE_SOUND, values, selection, selectionArgs);
        } else {
            database.insert(DatabaseHelper.TABLE_SOUND, null, values);
        }
    }

    // Retrieve a sound by ID
    public Sound getSoundById(int id) {
        String[] columns = {
                DatabaseHelper.COLUMN_SOUND_ID,
                DatabaseHelper.COLUMN_SOUND_NAME,
                DatabaseHelper.COLUMN_SOUND_SOUNDID
        };
        String selection = DatabaseHelper.COLUMN_SOUND_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query(DatabaseHelper.TABLE_SOUND, columns, selection, selectionArgs, null, null, null);

        Sound sound = null;
        if (cursor != null && cursor.moveToFirst()) {
            sound = cursorToSound(cursor);
            cursor.close();
        }
        return sound;
    }

    public Sound getSoundBySoundId(int soundId) {
        String[] columns = {
                DatabaseHelper.COLUMN_SOUND_ID,
                DatabaseHelper.COLUMN_SOUND_NAME,
                DatabaseHelper.COLUMN_SOUND_SOUNDID
        };
        String selection = DatabaseHelper.COLUMN_SOUND_SOUNDID + " = ?";
        String[] selectionArgs = { String.valueOf(soundId) };

        Cursor cursor = database.query(DatabaseHelper.TABLE_SOUND, columns, selection, selectionArgs, null, null, null);

        Sound sound = null;
        if (cursor != null && cursor.moveToFirst()) {
            sound = cursorToSound(cursor);
            cursor.close();
        }
        return sound;
    }

    // Retrieve all sounds
    public List<Sound> getAllSounds() {
        List<Sound> sounds = new ArrayList<>();

        String[] columns = {
                DatabaseHelper.COLUMN_SOUND_ID,
                DatabaseHelper.COLUMN_SOUND_NAME,
                DatabaseHelper.COLUMN_SOUND_SOUNDID
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_SOUND, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Sound sound = cursorToSound(cursor);
                sounds.add(sound);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return sounds;
    }

    // Common method to convert cursor to Sound object
    private Sound cursorToSound(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOUND_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOUND_NAME));
        int soundId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOUND_SOUNDID));
        return new Sound(id, name, soundId);
    }

    // Delete a sound by ID
    public int deleteSound(long id) {
        return database.delete(DatabaseHelper.TABLE_SOUND, DatabaseHelper.COLUMN_SOUND_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }
}
