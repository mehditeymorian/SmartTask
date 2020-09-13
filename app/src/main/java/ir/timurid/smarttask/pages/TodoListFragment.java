package ir.timurid.smarttask.pages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.OnTodoListSwipe;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.databinding.LayoutTodolistBinding;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.Delay;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.TodoListVM;
import lombok.Getter;


public class TodoListFragment extends Fragment implements TodoAdapter.OnTodoItemListener {
    public final int OPTION_SNAKE_BAR_DELAY = 2000;
    @Getter
    private TodoListVM viewModel;
    @Getter
    private AddTodoVM addTodoVM;
    @Getter
    private LayoutTodolistBinding binding;
    private TodoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, TodoListVM.class);
        addTodoVM = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, AddTodoVM.class);
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


        new OnTodoListSwipe(this);

    }


    //region OnTodoClick
    @Override
    public void onTodoClick(Todo todo) {
        Bundle bundle = new Bundle();
        bundle.putLong(TodoDetailFragment.TODO_ID, todo.getInfo().getTodoId());
        NavigationManager.navigate(this, bundle).accept(R.id.navigation_todoList_todoDetail);


    }

    @Override
    public void onTodoOptionsClick(Todo todo, int position) {
        DialogInterface.OnClickListener onOptionsSwitch = (dialog, which) -> {
            switch(which){
                case 0: // DELETE
                    deleteOption(todo, position);
                    break;
                case 1: // EDIT
                    editOption(todo);
                    break;
                case 2:
                default: // DONE
                    doneOption(todo, position);
            }
        };


        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.title_todoOptions)
                .setItems(R.array.todoListOptions, onOptionsSwitch)
                .show();
    }

    public void doneOption(Todo todo, int position) {
        adapter.deleteItemAt(position);

        Observable<Long> editObservable = Delay.getObservableForTime(OPTION_SNAKE_BAR_DELAY);

        Disposable subscribe = editObservable.subscribe(aLong -> viewModel.setTodoDone(todo));

        View.OnClickListener onUndoBtnClick = v -> {
            adapter.addItemAt(todo, position);
            subscribe.dispose();
        };

        MainActivity mainActivity = MainActivity.get(this);
        View coordinator = mainActivity.binding.coordinatorLayout;

        Snackbar.make(coordinator, R.string.title_done,OPTION_SNAKE_BAR_DELAY)
                .setAnchorView(mainActivity.binding.addTodoBtn)
                .setAction(R.string.action_undo, onUndoBtnClick).show();
    }

    private void editOption(Todo todo) {
        addTodoVM.setEditTodo(todo);
        MainActivity.get(this).showAddTodoLayout(null);

    }

    private void deleteOption(Todo todo, int position) {
       adapter.deleteItemAt(position);

        Observable<Long> deleteObservable = Delay.getObservableForTime(OPTION_SNAKE_BAR_DELAY);

        Disposable subscribe = deleteObservable.subscribe(aLong -> viewModel.delete(todo));

        View.OnClickListener onUndoBtnClick = v -> {
            adapter.addItemAt(todo, position);
            subscribe.dispose();
        };

        MainActivity mainActivity = MainActivity.get(this);
        View coordinator = mainActivity.binding.coordinatorLayout;

        Snackbar.make(coordinator, R.string.title_deleted,OPTION_SNAKE_BAR_DELAY)
                .setAnchorView(mainActivity.binding.addTodoBtn)
                .setAction(R.string.action_undo, onUndoBtnClick).show();
    }
    //endregion
}