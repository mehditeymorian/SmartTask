package ir.timurid.smarttask.pages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.OnTodoListSwipe;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.databinding.LayoutTodolistBinding;
import ir.timurid.smarttask.di.TodoListModule;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.Delay;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.viewModel.AddTodoVM;
import ir.timurid.smarttask.viewModel.TodoListVM;
import lombok.Getter;


@AndroidEntryPoint
public class TodoListFragment extends Fragment implements TodoAdapter.OnTodoItemListener {
    public final int SNAKE_BAR_DELAY = 2000;

    @Inject
    Lazy<TodoListVM> viewModel;
    @Inject
    Lazy<AddTodoVM> addTodoVM;
    @Inject
    @TodoListModule.TodoListAdapter
    Lazy<TodoAdapter> adapter;

    @Getter
    private LayoutTodolistBinding binding;


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
        binding.setViewModel(getViewModel());

        binding.todoListRecyclerView.setAdapter(getAdapter());

        getViewModel().getUndoneTodoList().observe(getViewLifecycleOwner(), this::onUndoneTodoListChange);

        getViewModel().getDoneTodoNotification().observe(getViewLifecycleOwner(), this::onChanged);


        new OnTodoListSwipe(this);

    }

    private void onUndoneTodoListChange(List<Todo> todos) {
        getAdapter().submitList(todos);
        getViewModel().getIsListEmpty().set(todos.isEmpty());
    }

    private void onChanged(Boolean todoDone) {
        if (todoDone) {
            binding.doneAnimationView.playAnimation();
            getViewModel().getDoneTodoNotification().setValue(false);
        }
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
        getAdapter().deleteItemAt(position);

        Observable<Long> editObservable = Delay.getObservableForTime(SNAKE_BAR_DELAY);

        Disposable subscribe = editObservable.subscribe(aLong -> getViewModel().setTodoDone(todo));

        View.OnClickListener onUndoBtnClick = v -> {
            getAdapter().addItemAt(todo, position);
            subscribe.dispose();
        };

        MainActivity mainActivity = MainActivity.get(this);
        View coordinator = mainActivity.binding.coordinatorLayout;

        Snackbar.make(coordinator, R.string.title_done, SNAKE_BAR_DELAY)
                .setAnchorView(mainActivity.binding.addTodoBtn)
                .setAction(R.string.action_undo, onUndoBtnClick).show();
    }

    private void editOption(Todo todo) {
        getAddTodoViewModel().setEditTodo(todo);
        MainActivity.get(this).showAddTodoLayout(null);

    }

    private void deleteOption(Todo todo, int position) {
       getAdapter().deleteItemAt(position);

        Observable<Long> deleteObservable = Delay.getObservableForTime(SNAKE_BAR_DELAY);

        Disposable subscribe = deleteObservable.subscribe(aLong -> getViewModel().delete(todo));

        View.OnClickListener onUndoBtnClick = v -> {
            getAdapter().addItemAt(todo, position);
            subscribe.dispose();
        };

        MainActivity mainActivity = MainActivity.get(this);
        View coordinator = mainActivity.binding.coordinatorLayout;

        Snackbar.make(coordinator, R.string.title_deleted, SNAKE_BAR_DELAY)
                .setAnchorView(mainActivity.binding.addTodoBtn)
                .setAction(R.string.action_undo, onUndoBtnClick).show();
    }
    //endregion

    public TodoListVM getViewModel() {
        return viewModel.get();
    }

    public AddTodoVM getAddTodoViewModel() {
        return addTodoVM.get();
    }

    public TodoAdapter getAdapter() {
        return adapter.get();
    }

}