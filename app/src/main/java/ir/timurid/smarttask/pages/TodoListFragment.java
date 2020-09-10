package ir.timurid.smarttask.pages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.OnTodoListSwipe;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.databinding.LayoutTodolistBinding;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.TodoListVM;


public class TodoListFragment extends Fragment implements TodoAdapter.OnTodoItemListener {

    private TodoListVM viewModel;
    private LayoutTodolistBinding binding;
    private TodoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, TodoListVM.class);
        adapter = new TodoAdapter(this, viewModel.getPrioritiesColors());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LayoutTodolistBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setParent(this);
        binding.setViewModel(viewModel);

        binding.todoListRecyclerView.setAdapter(adapter);

        viewModel.getUndoneTodoList().observe(getViewLifecycleOwner(), todos -> {
            adapter.submitList(todos);
            viewModel.getIsListEmpty().set(todos.isEmpty());
        });

        viewModel.getDoneTodoNotification().observe(getViewLifecycleOwner(),todoDone -> {
            if (todoDone) {
                binding.doneAnimationView.playAnimation();
                viewModel.getDoneTodoNotification().setValue(false);
            }
        });


        new OnTodoListSwipe(binding.todoListRecyclerView, viewModel);
    }


    @Override
    public void onTodoClick(Todo todo) {
        Bundle bundle = new Bundle();
        bundle.putLong(TodoDetailFragment.TODO_ID, todo.getInfo().getTodoId());
        NavigationManager.navigate(this, bundle).accept(R.id.navigation_todoList_todoDetail);


    }

    @Override
    public void onTodoOptionsClick(Todo todo) {
        DialogInterface.OnClickListener onOptionsSwitch = (dialog, which) -> {
            switch(which){
                case 0: // DELETE
                    viewModel.delete(todo);
                    break;

                case 1: // EDIT
                    VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, AddTodoVM.class).setEditTodo(todo);
                    ((MainActivity) requireActivity()).showAddTodoLayout(null);
                    break;
                case 2:
                default: // DONE
                    viewModel.setTodoDone(todo);
            }
        };

        new AlertDialog.Builder(requireContext())
                .setItems(R.array.todoListOptions, onOptionsSwitch)
                .show();
    }
}