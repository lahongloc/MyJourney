package com.example.personalschedulemanagementapp.ui.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalschedulemanagementapp.EditUserActivity;
import com.example.personalschedulemanagementapp.R;
import com.example.personalschedulemanagementapp.dao.UserDAO;
import com.example.personalschedulemanagementapp.entity.User;


import java.util.List;

public class UserAdapter extends BaseAdapter {
    private final Context context;
    private final List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        User user = getItem(position);

        if (user != null) {
            TextView textViewUsername =  convertView.findViewById(R.id.textViewUsername);
            ImageButton buttonEdit =  convertView.findViewById(R.id.buttonEdit);
            ImageButton buttonDelete =  convertView.findViewById(R.id.buttonDelete);

            textViewUsername.setText(user.getFullName());

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditUserActivity.class);

                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("password", user.getPassword());

                    context.startActivity(intent);
                }
            });

            buttonDelete.setOnClickListener(view -> {
                UserDAO userDAO = new UserDAO(view.getContext());
                long result = userDAO.deleteUserByUsername(user.getUsername());
                if (result > 0) {
                    // Deletion was successful
                    users.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Deletion failed
                    Toast.makeText(context, "User deletion failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }
}
