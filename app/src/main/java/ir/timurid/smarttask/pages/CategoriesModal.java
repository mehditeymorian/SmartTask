package ir.timurid.smarttask.pages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.CategoriesAdapter;
import ir.timurid.smarttask.databinding.LayoutCategoriesBinding;
import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.CategoriesVM;
import lombok.Getter;

import static ir.timurid.smarttask.model.Category.DEFAULT_CATEGORY_ID;


@AndroidEntryPoint
public class CategoriesModal extends BottomSheetDialogFragment implements CategoriesAdapter.OnItemClickListener {
    public static final String MODE_SELECTION = "MODE_SELECTION";

    private LayoutCategoriesBinding binding;

    @Inject
    CategoriesVM viewModel;

    @Inject
    Lazy<AddTodoVM> addTodoViewModel;

    @Inject
    CategoriesAdapter categoriesAdapter;

    @Inject
    @Getter
    boolean isSelectionModeActive;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LayoutCategoriesBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setParent(this);
        binding.setIsListEmpty(true);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.categoriesRecyclerView.setLayoutManager(layoutManager);
        binding.categoriesRecyclerView.setAdapter(categoriesAdapter);

        viewModel.getCategories().observe(this, list -> {
            binding.setIsListEmpty(list.size() == 0);
            categoriesAdapter.submitList(list);
        });
    }

    @SuppressWarnings("unused")
    public void onAddCategoryBtnClick(View view) {
        NavigationManager.navigate(this).accept(R.id.navigation_categories_addCategory);
    }

    // on categories click in the recyclerView
    @Override
    public void onItemClick(Category category) {
        if (isSelectionModeActive()) selectCategory(category);
        else showItemMenuDialog(category);
    }



    private void showItemMenuDialog(Category category) {
        if (category.getCategoryId() == DEFAULT_CATEGORY_ID)
            return;


        DialogInterface.OnClickListener onMenuItemClick = (dialog, which) -> {
            switch (which) {
                case 0: // DELETE
                    deleteCategory(category);
                    break;
                case 1: // EDIT
                    editCategory(category);
                    break;
            }
        };

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.title_categoryOptions)
                .setItems(R.array.category_menu, onMenuItemClick)
                .show();
    }

    private void editCategory(Category category) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AddCategoryModal.EDIT_MODE,true);
        bundle.putLong(AddCategoryModal.EDIT_MODE_ID, category.getCategoryId());
        bundle.putString(AddCategoryModal.EDIT_MODE_TITLE, category.getTitle());
        bundle.putString(AddCategoryModal.EDIT_MODE_COLOR, category.getColor());
        NavigationManager.navigate(this, bundle).accept(R.id.navigation_categories_addCategory);
    }

    private void deleteCategory(Category category) {

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.title_deleteCategory)
                .setMessage(R.string.desc_deleteCategory)
                .setPositiveButton(R.string.action_delete, (dialog, which) -> viewModel.deleteCategory(category))
                .setNegativeButton(R.string.action_no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void selectCategory(Category category) {
        addTodoViewModel.get().getCategoryField().set(category);
        NavigationManager.popBackFrom(this);
    }

}