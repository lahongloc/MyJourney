package com.example.personalschedulemanagementapp.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.personalschedulemanagementapp.RegisterActivity;
import com.example.personalschedulemanagementapp.ScheduleActivity;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentUserBinding;

import com.example.personalschedulemanagementapp.entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private ListView listViewUsers;
    private UserAdapter userAdapter;
    private List<User> users;
    private UserDAO userDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewUsers = binding.listViewUsers;

        userDAO = new UserDAO(binding.getRoot().getContext());
        users = userDAO.getUsers();

        userAdapter = new UserAdapter(requireContext(), users);

        listViewUsers.setAdapter(userAdapter);

        final FloatingActionButton addUser = binding.fabAddUser;
        addUser.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), RegisterActivity.class);
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