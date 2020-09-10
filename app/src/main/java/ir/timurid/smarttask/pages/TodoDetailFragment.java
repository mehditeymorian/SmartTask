package ir.timurid.smarttask.pages;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.LayoutTodoDetailBinding;
import ir.timurid.smarttask.extra.BindingAdapters;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.ColorUtils;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.TodoDetailVM;


public class TodoDetailFragment extends Fragment {
    public static final String TODO_ID = "TODO_ID";
    private TodoDetailVM viewModel;
    private AddTodoVM addTodoVM;
    private LayoutTodoDetailBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider.Factory factory = getTodoDetailFactory(getTodoId());
        viewModel = VMProvider.getModelFactory(this, VMProvider.TODO_DETAIL_GRAPH, TodoDetailVM.class, factory);
        addTodoVM = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, AddTodoVM.class);

    }

    private ViewModelProvider.Factory getTodoDetailFactory(long todoId) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TodoDetailVM(getActivity().getApplication(), todoId);
            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LayoutTodoDetailBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setParent(this);
        binding.setViewModel(viewModel);

        viewModel.getTodo().observe(getViewLifecycleOwner(), this::onTodoReceive);

        binding.setPrioritiesRes(getResources().getStringArray(R.array.priorities));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (addTodoVM.getIsEditMode().get())
            addTodoVM.dismissEditMode();
    }

    private void onTodoReceive(Todo todo) {
        binding.setTodo(todo);

        int priorityIconRes = BindingAdapters.prioritiesIconsRes[todo.getInfo().getPriority()];
        Drawable priorityIcon = ContextCompat.getDrawable(requireContext(), priorityIconRes);
        binding.priorityTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(priorityIcon, null, null, null);

        int[] prioritiesColors = getResources().getIntArray(R.array.prioritiesColors);
        int priorityColor = prioritiesColors[todo.getInfo().getPriority()];
        TextViewCompat.setCompoundDrawableTintList(binding.priorityTextView, ColorUtils.getColorStateList(priorityColor));


    }

    public void onCloseBtn(View view) {
        NavigationManager.popBackFrom(this);
    }

    public void editTodo(Todo todo) {
        addTodoVM.setEditTodo(todo);
        ((MainActivity) requireActivity()).showAddTodoLayout(null);
    }

    public long getTodoId() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            NavigationManager.popBackFrom(this);
            return -1;
        }

        return arguments.getLong(TODO_ID);
    }

}