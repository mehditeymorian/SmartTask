package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.transition.MaterialFadeThrough;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.databinding.LayoutDoneTodolistBinding;
import ir.timurid.smarttask.di.TodoListModule;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.TodoListVM;


@AndroidEntryPoint
public class DoneTodoListFragment extends Fragment implements TodoAdapter.OnTodoItemListener {

    private LayoutDoneTodolistBinding binding;

    @Inject
    TodoListVM viewModel;

    @Inject
    @TodoListModule.DoneTodoListAdapter
    TodoAdapter todoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialFadeThrough());
        setExitTransition(new MaterialFadeThrough());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = LayoutDoneTodolistBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(todoAdapter);

        binding.setIsListEmpty(true);
        binding.setEmptyAnimRes(R.raw.done_todolist_anim);

        viewModel.getDoneTodoList().observe(getViewLifecycleOwner(),todos -> {
            todoAdapter.submitList(todos);
            binding.setIsListEmpty(todos.isEmpty());
        });
    }

    @Override
    public void onTodoClick(Todo todo) {
        Bundle bundle = new Bundle();
        bundle.putLong(TodoDetailFragment.TODO_ID, todo.getInfo().getTodoId());
        NavigationManager.navigate(this, bundle).accept(R.id.navigation_doneTodoList_todoDetail);
    }

    @Override
    public void onTodoOptionsClick(Todo todo, int position) {

    }
}