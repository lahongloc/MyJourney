package com.example.personalschedulemanagementapp.ui.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.dao.ScheduleDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    private BarChart barChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barChart = binding.barChart;

        setupListeners();

        return root;
    }

    private void setupListeners() {
        // Lắng nghe sự kiện click của nút Category
        binding.btnCategory.setOnClickListener(v -> {
            ScheduleDAO scheduleDAO = new ScheduleDAO(binding.getRoot().getContext());
            updateChart(scheduleDAO.getScheduleCountByCategory(),
                    "Số lượng lịch trình theo loại");
        });

        // Lắng nghe sự kiện click của nút Date
        binding.btnDate.setOnClickListener(v -> openDateRangePicker());

        // Lắng nghe sự kiện click của nút Time
        binding.btnTime.setOnClickListener(v -> openDatePicker());
    }

    private void openDateRangePicker() {
        // Chọn ngày bắt đầu
        MaterialDatePicker<Long> startDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày bắt đầu")
                .build();

        startDatePicker.addOnPositiveButtonClickListener(startSelection -> {
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(startSelection);

            // Chọn ngày kết thúc
            MaterialDatePicker<Long> endDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày kết thúc")
                    .build();

            endDatePicker.addOnPositiveButtonClickListener(endSelection -> {
                Calendar endDate = Calendar.getInstance();
                endDate.setTimeInMillis(endSelection);

                ScheduleDAO scheduleDAO = new ScheduleDAO(binding.getRoot().getContext());
                updateChart(scheduleDAO.getScheduleCountByDateRange(startDate, endDate),
                        "Số lượng lịch trình theo ngày");
            });

            endDatePicker.show(getParentFragmentManager(), "END_DATE_PICKER");
        });

        startDatePicker.show(getParentFragmentManager(), "START_DATE_PICKER");
    }

    private void openDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(selection);

            ScheduleDAO scheduleDAO = new ScheduleDAO(binding.getRoot().getContext());
            updateChart(scheduleDAO.getScheduleCountByHourOfDate(date),
                    "Số lượng lịch trình theo giờ của 1 ngày");
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void updateChart(Map<String, Integer> statistic, String title) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        float index = 0;

        // Tạo dữ liệu cho biểu đồ
        for (Map.Entry<String, Integer> entry : statistic.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue()));
            labels.add(entry.getKey());
        }

        BarDataSet dataSet = new BarDataSet(entries, title);

        // Random màu cho các cột
        Random random = new Random();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colors.add(color);
        }
        dataSet.setColors(colors);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Điều chỉnh chiều rộng cột

        barChart.setData(barData);
        barChart.setFitBars(true); // Đảm bảo các cột vừa với không gian

        // Cấu hình trục X
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false); // Tắt lưới của trục X

        // Cấu hình trục Y
        barChart.getAxisLeft().setLabelCount(2, false);
        barChart.getAxisLeft().setDrawGridLines(false); // Tắt lưới của trục Y trái
        barChart.getAxisRight().setEnabled(false); // Tắt trục Y phải
        barChart.getAxisRight().setDrawGridLines(false); // Tắt lưới của trục Y phải (nếu có)

        // Cấu hình biểu đồ
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
