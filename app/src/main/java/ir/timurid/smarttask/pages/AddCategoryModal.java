package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.LayoutAddCategoryBinding;
import ir.timurid.smarttask.utils.Delay;
import ir.timurid.smarttask.utils.KeyboardHelper;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddCategoryVM;


@AndroidEntryPoint
public class AddCategoryModal extends BottomSheetDialogFragment {
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String EDIT_MODE_COLOR = "EDIT_MODE_COLOR";
    public static final String EDIT_MODE_TITLE = "EDIT_MODE_TITLE";
    public static final String EDIT_MODE_ID = "EDIT_MODE_ID";

    private LayoutAddCategoryBinding binding;

    @Inject
    AddCategoryVM viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LayoutAddCategoryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setParent(this);
        binding.setViewModel(viewModel);
        binding.titleLayout.setStartIconOnClickListener(v ->  NavigationManager.navigate(this).accept(R.id.navigation_addCategory_colorPicker));
        binding.titleLayout.setEndIconOnClickListener(this::onAddBtnClick);
        binding.titleInput.requestFocus();
    }



    @Override
    public void onResume() {
        super.onResume();
        Delay.forTime(250).andThen(() -> {
            KeyboardHelper.showKeyboard(getContext(), binding.titleInput);
            KeyboardHelper.setSelectionAtEndEditText(binding.titleInput);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.titleInput.clearFocus();
        KeyboardHelper.hideKeyboard(requireContext(),binding.titleInput);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.clearStates();
    }

    public void onAddBtnClick(View view) {
        viewModel.insertCategory();
        NavigationManager.popBackFrom(this);
    }

}