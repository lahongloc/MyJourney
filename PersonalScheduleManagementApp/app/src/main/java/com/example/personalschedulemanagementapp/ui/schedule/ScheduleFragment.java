package com.example.personalschedulemanagementapp.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.ScheduleActivity;
import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentScheduleBinding;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    private ListView listViewSchedules;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> schedules;
    private ScheduleDAO scheduleDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewSchedules = binding.listViewSchedules;

        scheduleDAO = new ScheduleDAO(binding.getRoot().getContext());
        schedules = scheduleDAO.getAllSchedules(binding.getRoot().getContext());
        schedules = schedules.stream().filter(
                schedule -> schedule.getStatus().equals(Status.WAITING.name()))
                .collect(Collectors.toList());
        schedules.sort((a, b) -> Long.compare(b.getTime().getTimeInMillis(), a.getTime().getTimeInMillis()));

        scheduleAdapter = new ScheduleAdapter(requireContext(), schedules, schedule -> {
            // Xử lý sự kiện nhấn vào schedule tại đây
            Intent intent = new Intent(requireContext(), ScheduleActivity.class);
            intent.putExtra("SCHEDULE_ID", schedule.getId()); // Truyền thông tin schedule nếu cần
            startActivity(intent);
        }, position -> {
            // Xử lý sự kiện nhấn nút xóa
            Schedule scheduleToDelete = scheduleAdapter.getItem(position);
            if (scheduleToDelete != null) {
                scheduleToDelete.cancelAlarmsForSchedule(binding.getRoot().getContext());
                int result = scheduleDAO.deleteSchedule(scheduleToDelete.getId()); // Xóa dữ liệu khỏi cơ sở dữ liệu
                if (result > 0) {
                    schedules.remove(position); // Xóa mục khỏi danh sách
                    scheduleAdapter.notifyDataSetChanged(); // Cập nhật adapter
                    Toast.makeText(binding.getRoot().getContext(), "Xóa lịch trình thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(binding.getRoot().getContext(), "Xóa lịch trình thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewSchedules.setAdapter(scheduleAdapter);

        final FloatingActionButton addSchedule = binding.btnAddSchedule;
        addSchedule.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), ScheduleActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
