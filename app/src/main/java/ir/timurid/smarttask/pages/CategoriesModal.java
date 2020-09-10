package ir.timurid.smarttask.pages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.CategoriesAdapter;
import ir.timurid.smarttask.databinding.LayoutCategoriesBinding;
import ir.timurid.smarttask.model.Category;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.CategoriesVM;

import static ir.timurid.smarttask.model.Category.DEFAULT_CATEGORY_ID;


public class CategoriesModal extends BottomSheetDialogFragment implements CategoriesAdapter.OnItemClickListener {
    public static final String MODE_SELECTION = "MODE_SELECTION";

    private LayoutCategoriesBinding binding;
    private CategoriesVM viewModel;

    private AddTodoVM addTodoViewModel;

    private CategoriesAdapter categoriesAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, CategoriesVM.class);
        categoriesAdapter = new CategoriesAdapter(this);

        if (isSelectionModeActive())
            addTodoViewModel = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, AddTodoVM.class);
    }


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

        binding.categoriesRecyclerView.setLayoutManager(getRecyclerViewLayoutManager());
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
        if (isSelectionModeActive()) {
            addTodoViewModel.getCategoryField().set(category);
            NavigationManager.popBackFrom(this);
        } else showItemMenuDialog(category);
    }

    private void showItemMenuDialog(Category category) {
        if (category.getCategoryId() == DEFAULT_CATEGORY_ID)
            return;


        DialogInterface.OnClickListener onMenuItemClick = (dialog, which) -> {
            switch (which) {
                case 0: // EDIT
                    editCategory(category);
                    break;
                case 1: // DELETE
                    deleteCategory(category);
                    break;
            }
        };
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_category_menu)
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

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.title_deleteCategory)
                .setMessage(R.string.desc_deleteCategory)
                .setPositiveButton(R.string.action_yes, (dialog, which) -> viewModel.deleteCategory(category))
                .setNegativeButton(R.string.action_no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(requireContext(), 2);
    }


    private boolean isSelectionModeActive() {
        Bundle arguments = getArguments();
        if (arguments == null) return false;
        return arguments.getBoolean(MODE_SELECTION, false);
    }
}