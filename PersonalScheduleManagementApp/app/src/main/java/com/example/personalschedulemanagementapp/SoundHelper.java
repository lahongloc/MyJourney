package com.example.personalschedulemanagementapp;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundHelper {
    private MediaPlayer mediaPlayer;

    public void playNotificationSound(Context context, int soundId) {
        // Giải phóng MediaPlayer nếu đã có âm thanh đang phát
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Tạo mới MediaPlayer với file âm thanh trong thư mục raw
        mediaPlayer = MediaPlayer.create(context, soundId);
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
