package com.example.personalschedulemanagementapp.ui.sound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.personalschedulemanagementapp.R;
import com.example.personalschedulemanagementapp.entity.Sound;

import java.util.List;

public class SoundAdapter extends ArrayAdapter<Sound> {

    private OnSoundClickListener deleteListener;
    private OnSoundClickListener updateListener;

    public interface OnSoundClickListener {
        void onClick(Sound sound);
    }

    public SoundAdapter(Context context, List<Sound> sounds, OnSoundClickListener deleteListener, OnSoundClickListener updateListener) {
        super(context, 0, sounds);
        this.deleteListener = deleteListener;
        this.updateListener = updateListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sound, parent, false);
        }

        Sound sound = getItem(position);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);
        ImageButton buttonUpdate = convertView.findViewById(R.id.buttonUpdate);

        textViewName.setText(sound.getName());

        // Xử lý sự kiện xóa âm thanh
        buttonDelete.setOnClickListener(v -> deleteListener.onClick(sound));

        // Xử lý sự kiện cập nhật âm thanh
        buttonUpdate.setOnClickListener(v -> updateListener.onClick(sound));

        return convertView;
    }
}
