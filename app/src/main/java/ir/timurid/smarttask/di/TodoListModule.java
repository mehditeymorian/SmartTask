package ir.timurid.smarttask.di;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import ir.timurid.smarttask.adapter.TodoAdapter;
import ir.timurid.smarttask.pages.DoneTodoListFragment;
import ir.timurid.smarttask.pages.TodoListFragment;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.TodoListVM;

@Module
@InstallIn(FragmentComponent.class)
public class TodoListModule {


    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DoneTodoListAdapter {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TodoListAdapter {}


    @Provides
    @TodoListAdapter
    public static TodoAdapter provideTodoListAdapter(Fragment fragment, TodoListVM viewModel) {
        return new TodoAdapter( (TodoListFragment) fragment, viewModel.getPrioritiesColors());
    }

    @Provides
    @DoneTodoListAdapter
    public static TodoAdapter provideDoneTodoListAdapter(Fragment fragment, TodoListVM viewModel) {
        TodoAdapter todoAdapter = new TodoAdapter((DoneTodoListFragment) fragment, viewModel.getPrioritiesColors());
        todoAdapter.setDoneList(true);
        return todoAdapter;
    }

    @Provides
    public static TodoListVM provideTodoListViewModel(Fragment fragment) {
        return VMProvider.getAndroidModel(fragment, VMProvider.MAIN_GRAPH, TodoListVM.class);
    }
}
