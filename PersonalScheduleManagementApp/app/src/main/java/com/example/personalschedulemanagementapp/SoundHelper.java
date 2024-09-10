package com.example.personalschedulemanagementapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.net.URL;

public class SoundHelper {
    private MediaPlayer mediaPlayer;

    public void playNotificationSound(Context context, int soundId) {
        // Giải phóng MediaPlayer nếu đã có âm thanh đang phát
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Tạo mới MediaPlayer với file âm thanh
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }

    public void playNotificationSound(Context context, Uri uri) {
        // Giải phóng MediaPlayer nếu đã có âm thanh đang phát
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Tạo mới MediaPlayer với file âm thanh
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.start();
    }

    // Phương thức để giải phóng MediaPlayer khi không còn sử dụng
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
