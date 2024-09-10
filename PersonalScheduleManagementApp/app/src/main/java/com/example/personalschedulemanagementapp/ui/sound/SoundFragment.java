package com.example.personalschedulemanagementapp.ui.sound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.R;
import com.example.personalschedulemanagementapp.SoundHelper;
import com.example.personalschedulemanagementapp.dao.SoundDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentSoundBinding;
import com.example.personalschedulemanagementapp.entity.Sound;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SoundFragment extends Fragment {

    private static final int PICK_SOUND_REQUEST = 1;

    private FragmentSoundBinding binding;
    private ListView listViewSounds;
    private List<Sound> sounds;
    private SoundDAO soundDAO;
    private SoundAdapter soundAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewSounds = binding.listViewSounds;

        soundDAO = new SoundDAO(requireContext());
        sounds = soundDAO.getAllSounds();

        soundAdapter = new SoundAdapter(requireContext(), sounds,
                sound -> {
                    // Xử lý xóa âm thanh
                    showDeleteConfirmationDialog(sound);
                },
                sound -> {
                    // Xử lý cập nhật âm thanh
                    showUpdateDialog(sound);
                });


        listViewSounds.setAdapter(soundAdapter);

        FloatingActionButton fabAddSound = binding.fabAddSound;
        fabAddSound.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_SOUND_REQUEST);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SOUND_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri soundUri = data.getData();
            if (soundUri != null) {
                String soundName = getFileName(soundUri);
                // Hiển thị dialog để nhập tên âm thanh mới
                showNameInputDialog(soundUri, soundName, null);
            }
        }
    }

    private void showUpdateDialog(Sound sound) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cập nhật tên âm thanh");

        // Thiết lập giao diện cho dialog
        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_input, null);
        final EditText input = viewInflated.findViewById(R.id.inputName);
        input.setText(sound.getName());
        builder.setView(viewInflated);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newName = input.getText().toString();
            sound.setName(newName);
            soundDAO.insertOrUpdateSound(sound); // Cập nhật âm thanh
            updateSoundList();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showNameInputDialog(Uri soundUri, String defaultName, Sound existingSound) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(existingSound == null ? "Nhập tên âm thanh" : "Cập nhật tên âm thanh");

        // Thiết lập giao diện cho dialog
        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_input, null);
        final EditText input = viewInflated.findViewById(R.id.inputName);
        input.setText(defaultName);
        builder.setView(viewInflated);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String soundName = input.getText().toString();
            Sound sound = existingSound != null ? existingSound : new Sound(soundName, soundUri);
            sound.setName(soundName);
            sound.setUri(soundUri); // Cập nhật URI nếu cần thiết
            soundDAO.insertOrUpdateSound(sound);
            updateSoundList();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showDeleteConfirmationDialog(Sound sound) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa âm thanh này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    soundDAO.deleteSound(sound.getId()); // Xóa âm thanh
                    updateSoundList();
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String getFileName(Uri uri) {
        String fileName = "";
        Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }

    private void updateSoundList() {
        sounds = soundDAO.getAllSounds();
        soundAdapter.clear();
        soundAdapter.addAll(sounds);
        soundAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (soundDAO != null) {
            soundDAO.close();
        }
        binding = null;
    }
}
