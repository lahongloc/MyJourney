package com.example.personalschedulemanagementapp.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.personalschedulemanagementapp.CategoryActivity;
import com.example.personalschedulemanagementapp.dao.CategoryDAO;
import com.example.personalschedulemanagementapp.databinding.FragmentCategoriesBinding;
import com.example.personalschedulemanagementapp.entity.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;
    private ListView listViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private CategoryDAO categoryDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewCategories = binding.listViewCategories;

        categoryDAO = new CategoryDAO(binding.getRoot().getContext());
        categories = categoryDAO.getAllCategories(binding.getRoot().getContext());

        categoryAdapter = new CategoryAdapter(requireContext(), categories, category -> {
            // Xử lý sự kiện nhấn vào category tại đây
            Intent intent = new Intent(requireContext(), CategoryActivity.class);
            intent.putExtra("CATEGORY_ID", category.getId()); // Truyền thông tin category nếu cần
            startActivity(intent);
        }, position -> {
            // Xử lý sự kiện nhấn nút xóa
            Category categoryToDelete = categoryAdapter.getItem(position);
            if (categoryToDelete != null) {
                int result = categoryDAO.deleteCategory(categoryToDelete.getId()); // Xóa dữ liệu khỏi cơ sở dữ liệu
                if (result > 0) {
                    categories.remove(position); // Xóa mục khỏi danh sách
                    categoryAdapter.notifyDataSetChanged(); // Cập nhật adapter
                    Toast.makeText(binding.getRoot().getContext(), "Xóa loại lịch trình thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(binding.getRoot().getContext(), "Xóa loại lịch trình thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewCategories.setAdapter(categoryAdapter);

        final FloatingActionButton addCategory = binding.btnAddCategory;
        addCategory.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), CategoryActivity.class);
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
