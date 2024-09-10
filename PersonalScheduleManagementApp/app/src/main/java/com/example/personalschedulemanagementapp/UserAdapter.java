//package com.example.personalschedulemanagementapp;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.personalschedulemanagementapp.dao.UserDAO;
//import com.example.personalschedulemanagementapp.entity.User;
//
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//
//    private Context context;
//    private List<User> userList;
//    private OnUserActionListener listener;
//    private UserDAO userDAO;
//
//    public interface OnUserActionListener {
//        void onEditClicked(User user);
//        void onDeleteClicked(User user);
//    }
//
//    public UserAdapter(Context context, List<User> userList, OnUserActionListener listener) {
//        this.context = context;
//        this.userList = userList;
//        this.listener = listener;
//        this.userDAO = new UserDAO(context);
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = userList.get(position);
//        holder.textViewUsername.setText(user.getFullName());
//        String username = user.getUsername();
//        String password = user.getPassword();
//
//        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, EditUserActivity.class);
//
//                intent.putExtra("username", username);
//                intent.putExtra("password", password);
//
//                context.startActivity(intent);
//            }
//        });
//        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long result = userDAO.deleteUserByUsername(username);
//                if (result > 0) {
//                    // Deletion was successful
//                    userList.remove(position);
//                    notifyItemRemoved(position);
//                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Deletion failed
//                    Toast.makeText(context, "User deletion failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView textViewUsername;
//        Button buttonEdit, buttonDelete;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewUsername = itemView.findViewById(R.id.textViewUsername);
//            buttonEdit = itemView.findViewById(R.id.buttonEdit);
//            buttonDelete = itemView.findViewById(R.id.buttonDelete);
//        }
//    }
//
//
//
//}
