package ir.timurid.smarttask.di;

import androidx.fragment.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import ir.timurid.smarttask.MainActivity;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;

@Module
@InstallIn(ActivityComponent.class)
class ActivityModule {



    @Provides
    public static AddTodoVM provideAddTodoViewModel(FragmentActivity activity) {
        return VMProvider.getAndroidModel(activity, VMProvider.MAIN_GRAPH, AddTodoVM.class);
    }

    @Provides
    public static MainActivity provideMainActivity(FragmentActivity fragmentActivity) {
        return (MainActivity) fragmentActivity;
    }
}
