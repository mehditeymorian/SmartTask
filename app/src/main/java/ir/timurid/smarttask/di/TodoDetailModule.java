package ir.timurid.smarttask.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import ir.timurid.smarttask.Application;
import ir.timurid.smarttask.R;
import ir.timurid.smarttask.pages.TodoDetailFragment;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.TodoDetailFactory;
import ir.timurid.smarttask.viewModel.TodoDetailVM;

@Module
@InstallIn(FragmentComponent.class)
public class TodoDetailModule {

    @Provides
    public static TodoDetailVM provideTodoDetailViewModel(Fragment fragment, TodoDetailFactory factory) {
        return VMProvider.getModelFactory(fragment, VMProvider.TODO_DETAIL_GRAPH, TodoDetailVM.class, factory);
    }


    @Provides
    public static long provideTodoId(Fragment fragment) {
        return ((TodoDetailFragment) fragment).getTodoId();
    }

    @Provides
    public static String[] providePriorities(@ApplicationContext Context context) {
        return context.getResources().getStringArray(R.array.priorities);
    }

    @Provides
    public static int[] providePrioritiesColors(@ApplicationContext Context context) {
        return context.getResources().getIntArray(R.array.prioritiesColors);
    }
}
