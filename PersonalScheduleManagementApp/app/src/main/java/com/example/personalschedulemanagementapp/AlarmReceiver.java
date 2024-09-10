package com.example.personalschedulemanagementapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.Status;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "SCHEDULE_NOTIFICATION_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        int scheduleId = intent.getIntExtra("SCHEDULE_ID", -1);
        if (scheduleId != -1) {
            // Lấy schedule từu scheduleId
            ScheduleDAO scheduleDAO = new ScheduleDAO(context);
            Schedule schedule = scheduleDAO.getScheduleById(scheduleId, context);
            schedule.setStatus(Status.NOTIFIED.name());
            scheduleDAO.insertOrUpdateSchedule(schedule);

            SoundHelper soundHelper = new SoundHelper();
            soundHelper.playNotificationSound(context, schedule.getCategory().getSound().getUri());

            // Tạo Notification Manager
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Tạo Notification Channel cho Android Oreo trở lên
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Schedule Notifications",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Channel for schedule notifications");
                notificationManager.createNotificationChannel(channel);
            }

            // Tạo PendingIntent để mở Activity khi nhấn vào thông báo
            Intent notificationIntent = new Intent(context, UserActivity.class); // Thay thế UserActivity bằng Activity bạn muốn mở
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Tạo thông báo
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp) // Thay thế ic_notifications_black_24dp bằng biểu tượng thông báo của bạn
                    .setContentTitle("Nhắc nhở lịch trình")
                    .setContentText("Bạn có lịch trình " + schedule.getTitle() + " sau " + schedule.getCategory().getRemindTime() + " phút nữa.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Hiển thị thông báo
            notificationManager.notify(scheduleId, builder.build());
        }
    }
}
