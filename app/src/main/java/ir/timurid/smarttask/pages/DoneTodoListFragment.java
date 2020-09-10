package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.databinding.LayoutDoneTodolistBinding;
import ir.timurid.smarttask.model.Todo;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.TodoListVM;


public class DoneTodoListFragment extends Fragment implements TodoAdapter.OnTodoItemListener {

    private LayoutDoneTodolistBinding binding;
    private TodoAdapter todoAdapter;
    private TodoListVM viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = VMProvider.getAndroidModel(this, VMProvider.MAIN_GRAPH, TodoListVM.class);
        todoAdapter = new TodoAdapter(this, viewModel.getPrioritiesColors());
        todoAdapter.setDoneList(true);
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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(todoAdapter);

        viewModel.getDoneTodoList().observe(getViewLifecycleOwner(),todoAdapter::submitList);
    }

    @Override
    public void onTodoClick(Todo todo) {
        Bundle bundle = new Bundle();
        bundle.putLong(TodoDetailFragment.TODO_ID, todo.getInfo().getTodoId());
        NavigationManager.navigate(this, bundle).accept(R.id.navigation_doneTodoList_todoDetail);
    }

    @Override
    public void onTodoOptionsClick(Todo todo) {

    }
}