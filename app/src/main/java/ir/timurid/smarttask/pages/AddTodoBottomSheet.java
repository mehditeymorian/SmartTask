package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.LayoutAddTodoBinding;
import ir.timurid.smarttask.utils.KeyboardHelper;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import lombok.Getter;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class AddTodoBottomSheet extends BottomSheetCallback {
    private LayoutAddTodoBinding binding;
    private MainActivity activity;
    @Getter
    private AddTodoVM viewModel;


    @SuppressWarnings("rawtypes")
    private BottomSheetBehavior bottomSheetBehavior;


    //region Class Initialization
    public AddTodoBottomSheet(LayoutAddTodoBinding binding, MainActivity activity) {
        this.binding = binding;
        this.activity = activity;
        dependenciesInit();
        bottomSheetInit();
        bindingInit();

    }

    private void dependenciesInit() {
        viewModel = VMProvider.getAndroidModel(activity, VMProvider.MAIN_GRAPH, AddTodoVM.class);
    }

    private void bottomSheetInit() {
        bottomSheetBehavior = from(binding.layout);
        bottomSheetBehavior.addBottomSheetCallback(this);
    }

    private void bindingInit() {
        binding.setParent(this);
        binding.setViewModel(viewModel);
        binding.setPrioritiesRes(viewModel.getPrioritiesRes());
        binding.deadlineChip.setOnCloseIconClickListener(v -> viewModel.setDeadline(null));
        binding.categoryChip.setOnCloseIconClickListener(v -> viewModel.getCategoryField().set(null));
        binding.titleLayout.setStartIconOnClickListener(v -> {
            viewModel.dismissEditMode();
            hide();
        });
        binding.titleLayout.setEndIconOnClickListener(v -> {
            viewModel.insertTodo();
            hide();
        });
        hide();
    }
    //endregion

    //region Deadline Chip
    @SuppressWarnings("unused")
    public void onDeadlineChipClick(View view) {

        PersianDatePickerDialog datePicker = getDatePicker();
        datePicker.setListener(new Listener() {
            @Override
            public void onDateSelected(PersianCalendar persianCalendar) {
                viewModel.setDeadline(persianCalendar.getTime());
            }

            @Override
            public void onDismissed() {

            }
        });
        datePicker.show();
    }


    private PersianDatePickerDialog getDatePicker() {
        PersianCalendar calendar = new PersianCalendar();
        return new PersianDatePickerDialog(activity)
                .setMinYear(calendar.getPersianYear());
    }
    //endregion

    //region Priority Chip
    @SuppressWarnings("unused")
    public void onPriorityChipClick(View view) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.title_priority)
                .setItems(viewModel.getPrioritiesRes(), (dialog, which) -> viewModel.getPriorityField().set(which))
                .setCancelable(true)
                .show();
    }
    //endregion

    @SuppressWarnings("unused")
    public void onCategoryChipClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CategoriesModal.MODE_SELECTION, true);

        NavigationManager.navigate(activity,bundle).accept(R.id.navigation_todoList_categories);
    }


    //region Hide Show BottomSheet
    public void show() {
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    public void hide() {
        bottomSheetBehavior.setState(STATE_HIDDEN);
    }

    public boolean isHidden() {
        return bottomSheetBehavior.getState() == STATE_HIDDEN;
    }


    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (newState == STATE_HIDDEN) {

            int destination = NavigationManager.getNavController(activity).getCurrentDestination().getId();
            if (destination == R.id.todoListFragment) activity.binding.addTodoBtn.show();

            KeyboardHelper.hideKeyboard(activity, binding.getRoot());

            binding.getRoot().clearFocus();

        } else if (newState == STATE_COLLAPSED) {
            binding.titleInput.requestFocus();

            Editable titleVal = binding.titleInput.getText();
            binding.titleInput.setSelection(titleVal != null ? titleVal.length() : 0);

            KeyboardHelper.showKeyboard(activity,binding.titleInput);
        } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            binding.descriptionInput.requestFocus();
        }

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }


    //endregion

    public void onDestroy() {
        activity = null;
        binding.unbind();
    }
}
