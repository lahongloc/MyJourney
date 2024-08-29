package com.example.personalschedulemanagementapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.Status;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("SCHEDULE_ID", -1);
        if (id == -1) {
            return;
        }
        ScheduleDAO scheduleDAO = new ScheduleDAO(context);
        Schedule schedule = scheduleDAO.getScheduleById(id, context);

        if (schedule != null) {
            schedule.setStatus(Status.NOTIFIED.name());
            scheduleDAO.insertOrUpdateSchedule(schedule);

            SoundHelper soundHelper = new SoundHelper();
            soundHelper.playNotificationSound(context, schedule.getCategory().getSound().getSoundId());

            Toast.makeText(context, "Sắp đến lịch trình: " + schedule.getTitle(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không tìm thấy lịch trình với ID: " + id, Toast.LENGTH_SHORT).show();
        }
    }
}
