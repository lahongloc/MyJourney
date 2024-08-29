package com.example.personalschedulemanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.personalschedulemanagementapp.entity.Sound;

import java.util.List;

public class SoundListAdapter extends ArrayAdapter<Sound> {

    private OnSoundClickListener listener;

    public interface OnSoundClickListener {
        void onDeleteClick(Sound sound);
    }

    public SoundListAdapter(Context context, List<Sound> sounds, OnSoundClickListener listener) {
        super(context, 0, sounds);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sound, parent, false);
        }

        Sound sound = getItem(position);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);

        textViewName.setText(sound.getName());
        buttonDelete.setOnClickListener(v -> listener.onDeleteClick(sound));

        return convertView;
    }
}

