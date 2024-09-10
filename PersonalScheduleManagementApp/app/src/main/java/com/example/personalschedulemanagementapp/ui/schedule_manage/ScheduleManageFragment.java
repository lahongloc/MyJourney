package com.example.personalschedulemanagementapp.ui.schedule_manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.ScheduleActivity;
import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentScheduleManageBinding;
import com.example.personalschedulemanagementapp.entity.Role;
import com.example.personalschedulemanagementapp.entity.Schedule;
import com.example.personalschedulemanagementapp.entity.User;
import com.example.personalschedulemanagementapp.ui.schedule.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScheduleManageFragment extends Fragment {

    private FragmentScheduleManageBinding binding;
    private ArrayAdapter<User> userAdapter;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> schedules;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleManageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUserDropdown();
        setupScheduleList();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật danh sách người dùng khi Fragment quay lại
        updateUserDropdown();
        // Đặt lại trạng thái của AutoCompleteTextView và ListView
        binding.userAutoCompleteTextView.setText("");  // Đặt lại AutoCompleteTextView
        binding.emptyStateTextView.setVisibility(View.VISIBLE);
        binding.listViewScheduleManage.setVisibility(View.GONE);
    }

    private void setupUserDropdown() {
        updateUserDropdown();

        binding.userAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userAdapter.getItem(position);
            if (selectedUser != null) {
                loadScheduleForUser(selectedUser);
            }
        });
    }

    private void updateUserDropdown() {
        UserDAO userDAO = new UserDAO(requireContext());
        List<User> users = userDAO.getUsersByRole(Role.USER.name());
        if (userAdapter == null) {
            userAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, users);
            binding.userAutoCompleteTextView.setAdapter(userAdapter);
        } else {
            userAdapter.clear();
            userAdapter.addAll(users);
            userAdapter.notifyDataSetChanged();
        }
    }

    private void setupScheduleList() {
        schedules = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(requireContext(), schedules, schedule -> {
            Intent intent = new Intent(requireContext(), ScheduleActivity.class);
            intent.putExtra("SCHEDULE_ID", schedule.getId());
            startActivity(intent);
        }, position -> {
            Schedule scheduleToDelete = scheduleAdapter.getItem(position);
            if (scheduleToDelete != null) {
                scheduleToDelete.cancelAlarmsForSchedule(requireContext());
                ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
                int result = scheduleDAO.deleteSchedule(scheduleToDelete.getId());
                if (result > 0) {
                    schedules.remove(scheduleToDelete);
                    scheduleAdapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Xóa lịch trình thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Xóa lịch trình thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.listViewScheduleManage.setAdapter(scheduleAdapter);
    }

    private void loadScheduleForUser(User user) {
        if (user != null) {
            binding.emptyStateTextView.setVisibility(View.GONE);
            binding.listViewScheduleManage.setVisibility(View.VISIBLE);

            schedules.clear();
            schedules.addAll(fetchSchedulesForUser(user));
            scheduleAdapter.notifyDataSetChanged();
        } else {
            binding.emptyStateTextView.setVisibility(View.VISIBLE);
            binding.listViewScheduleManage.setVisibility(View.GONE);
        }
    }

    private List<Schedule> fetchSchedulesForUser(User user) {
        ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
        return scheduleDAO.getSchedulesByUserId((int) user.getId(), requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
