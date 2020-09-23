package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.Lazy;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.LayoutAddTodoBinding;
import ir.timurid.smarttask.db.Preferences;
import ir.timurid.smarttask.utils.KeyboardHelper;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.viewModel.AddTodoVM;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class AddTodoBottomSheet extends BottomSheetCallback {
    private LayoutAddTodoBinding binding;
    private MainActivity activity;
    private Lazy<AddTodoVM> viewModel;


    @SuppressWarnings("rawtypes")
    private BottomSheetBehavior bottomSheetBehavior;


    //region Class Initialization
    @Inject
    public AddTodoBottomSheet(MainActivity activity, Lazy<AddTodoVM> viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    public void init() {
        this.binding = activity.binding.addTodoLayout;

        bottomSheetBehavior = from(binding.layout);
        bottomSheetBehavior.addBottomSheetCallback(this);


        binding.setParent(this);
        binding.setViewModel(getViewModel());
        binding.deadlineChip.setOnCloseIconClickListener(v -> getViewModel().setDeadline(null));
        binding.categoryChip.setOnCloseIconClickListener(v -> getViewModel().getCategoryField().set(null));
        binding.titleLayout.setStartIconOnClickListener(v -> {
            getViewModel().dismissEditMode();
            hide();
        });
        binding.titleLayout.setEndIconOnClickListener(v -> {
            boolean waterfallAddEnable = Preferences.getWaterfallTodoAdd(activity);
            boolean editMode = getViewModel().isEditMode();

            getViewModel().insertTodo();

            if (waterfallAddEnable && !editMode){
                getViewModel().clearFields();
                onCollapsedState();
            }else hide();
        });
        hide();
    }


    private AddTodoVM getViewModel() {
        return viewModel.get();
    }
    //endregion

    //region Deadline Chip
    @SuppressWarnings("unused")
    public void onDeadlineChipClick(View view) {

        PersianDatePickerDialog datePicker = getDatePicker();
        datePicker.setListener(new Listener() {
            @Override
            public void onDateSelected(PersianCalendar persianCalendar) {
                getViewModel().setDeadline(persianCalendar.getTime());
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
                .setItems(R.array.priorities, (dialog, which) -> getViewModel().getPriorityField().set(which))
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
        if (newState == STATE_HIDDEN) onHiddenState();
        else if (newState == STATE_COLLAPSED) onCollapsedState();
        else if (newState == BottomSheetBehavior.STATE_EXPANDED)
            binding.descriptionInput.requestFocus();

    }

    private void onHiddenState() {
        int destination = NavigationManager.getNavController(activity).getCurrentDestination().getId();
        if (destination == R.id.todoListFragment) activity.binding.addTodoBtn.show();

        KeyboardHelper.hideKeyboard(activity, binding.getRoot());

        binding.getRoot().clearFocus();
    }

    private void onCollapsedState() {
        TextInputEditText titleInput = binding.titleInput;
        titleInput.requestFocus();
        KeyboardHelper.setSelectionAtEndEditText(titleInput);
        KeyboardHelper.showKeyboard(activity, titleInput);
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
