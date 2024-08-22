package com.example.personalschedulemanagementapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.personalschedulemanagementapp.R;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends BaseAdapter {

    private final Context context;
    private final List<Schedule> schedules;
    private final OnItemClickListener onItemClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    // Interface để xử lý sự kiện nhấn
    public interface OnItemClickListener {
        void onItemClick(Schedule schedule);
    }

    // Interface để xử lý sự kiện nhấn nút xóa
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public ScheduleAdapter(Context context, List<Schedule> schedules, OnItemClickListener itemClickListener, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.schedules = schedules;
        this.onItemClickListener = itemClickListener;
        this.onDeleteClickListener = deleteClickListener;
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Schedule getItem(int position) {
        return schedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        }

        Schedule schedule = getItem(position);

        if (schedule != null) {
            TextView titleTextView = convertView.findViewById(R.id.tvScheduleTitle);
            TextView descriptionTextView = convertView.findViewById(R.id.tvScheduleDescription);
            TextView timeTextView = convertView.findViewById(R.id.tvScheduleTime);
            TextView categoryTextView = convertView.findViewById(R.id.tvScheduleCategory);
            Button deleteButton = convertView.findViewById(R.id.btnScheduleDelete);

            titleTextView.setText(schedule.getTitle());
            descriptionTextView.setText(schedule.getDescription());
            String timeFormat = DATE_FORMAT.format(schedule.getTime().getTime());
            timeTextView.setText("Time: " + timeFormat);
            if (schedule.getCategory() != null) {
                categoryTextView.setText("Category: " + schedule.getCategory().getName());
            } else {
                categoryTextView.setText("Category: No Category");
            }

            // Thiết lập sự kiện nhấn cho từng item
            convertView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(schedule);
                }
            });

            // Thiết lập sự kiện nhấn cho nút xóa
            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });
        }

        return convertView;
    }
}
