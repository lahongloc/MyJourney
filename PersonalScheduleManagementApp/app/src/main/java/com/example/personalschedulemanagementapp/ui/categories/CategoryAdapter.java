package com.example.personalschedulemanagementapp.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.personalschedulemanagementapp.R;
import com.example.personalschedulemanagementapp.entity.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private final Context context;
    private final List<Category> categories;
    private final OnItemClickListener onItemClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    // Interface để xử lý sự kiện nhấn
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    // Interface để xử lý sự kiện nhấn nút xóa
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public CategoryAdapter(Context context, List<Category> categories, OnItemClickListener itemClickListener, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.categories = categories;
        this.onItemClickListener = itemClickListener;
        this.onDeleteClickListener = deleteClickListener;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        }

        Category category = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.tvName);
        TextView descriptionTextView = convertView.findViewById(R.id.tvDescription);
        TextView remindTimeTextView = convertView.findViewById(R.id.tvRemindTime);
        TextView soundTextView = convertView.findViewById(R.id.tvSoundId);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);

        if (category != null) {
            nameTextView.setText(category.getName());
            descriptionTextView.setText(category.getDescription());
            remindTimeTextView.setText("Remind Time: " + category.getRemindTime() + " minutes");
            soundTextView.setText("Sound: " + category.getSound());
        }

        // Thiết lập sự kiện nhấn cho từng item
        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(category);
            }
        });

        // Thiết lập sự kiện nhấn cho nút xóa
        deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });

        return convertView;
    }
}
